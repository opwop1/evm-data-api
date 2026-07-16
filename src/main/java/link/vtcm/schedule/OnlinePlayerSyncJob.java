package link.vtcm.schedule;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.map.MapUtil;
import jodd.util.ThreadUtil;
import link.vtcm.api.TruckersMapApi;
import link.vtcm.common.constant.LockConstant;
import link.vtcm.common.util.SyncUtil;
import link.vtcm.domain.OnlinePlayer;
import link.vtcm.domain.OnlinePlayerHistory;
import link.vtcm.domain.PlayerMileage;
import link.vtcm.domain.Server;
import link.vtcm.domain.dto.TmpMapPlayer;
import link.vtcm.domain.dto.TmpMapResult;
import link.vtcm.mapper.OnlinePlayerHistoryMapper;
import link.vtcm.mapper.OnlinePlayerMapper;
import link.vtcm.service.PlayerMileageService;
import link.vtcm.service.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 在线玩家数据同步任务
 * @author zhangmj
 * @since 2025/7/1
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OnlinePlayerSyncJob {
    private final TruckersMapApi truckersMapApi;
    private final SyncUtil syncUtil;
    private final OnlinePlayerMapper onlinePlayerMapper;
    private final OnlinePlayerHistoryMapper onlinePlayerHistoryMapper;
    private final PlayerMileageService playerMileageService;
    private final ServerService serverService;

    @Scheduled(fixedDelay = 100)
    public void job() {
        TimeInterval timer = DateUtil.timer();
        log.info("job::开始同步在线玩家数据 time={}", DatePattern.NORM_DATETIME_FORMAT.format(DateUtil.date()));

        boolean isError = false;
        List<OnlinePlayer> onlinePlayerList = CollUtil.newArrayList();
        try {
            // 所有在线玩家
            TmpMapResult<List<TmpMapPlayer>> playerListResult = truckersMapApi.fullMap();
            if (!playerListResult.getStatus()) {
                log.info("job::获取在线玩家数据失败");
                return;
            }
            if (CollUtil.isEmpty(playerListResult.getData())) {
                log.info("job::获取在线玩家数据为空");
                return;
            }

            // 遍历处理玩家信息
            for (TmpMapPlayer tmpMapPlayer : playerListResult.getData()) {
                OnlinePlayer onlinePlayer = new OnlinePlayer();
                onlinePlayer.setTmpId(tmpMapPlayer.getMpId());
                onlinePlayer.setTmpName(tmpMapPlayer.getName());
                onlinePlayer.setServerPlayerId(tmpMapPlayer.getPlayerId());
                onlinePlayer.setServerId(tmpMapPlayer.getServerId());
                onlinePlayer.setAxisX(tmpMapPlayer.getX());
                onlinePlayer.setAxisY(tmpMapPlayer.getY());
                onlinePlayer.setHeading(tmpMapPlayer.getHeading());
                onlinePlayer.setUpdateTime(DateUtil.date(tmpMapPlayer.getTime() * 1000));
                onlinePlayerList.add(onlinePlayer);
            }
        } catch (Exception e) {
            log.error("job::同步在线玩家数据失败", e);
            isError = true;
        }

        // 保存数据
        syncUtil.wLook(LockConstant.SYNC_ONLINE_PLAYER, 3000L, () -> {
            onlinePlayerMapper.delete(null);
            CollUtil.split(onlinePlayerList, 800).forEach(onlinePlayerMapper::insertBatch);
            return null;
        });

        // 处理里程
        List<PlayerMileage> playerMileageList = this.playerMileageHandle(onlinePlayerList);

        // 保存在线历史数据
        List<OnlinePlayerHistory> onlinePlayerHistoryList = playerMileageList.parallelStream().map(item -> {
            OnlinePlayerHistory onlinePlayerHistory = new OnlinePlayerHistory();
            onlinePlayerHistory.setTmpId(item.getTmpId());
            onlinePlayerHistory.setServerId(item.getServerId());
            onlinePlayerHistory.setAxisX(item.getAxisX());
            onlinePlayerHistory.setAxisY(item.getAxisY());
            onlinePlayerHistory.setHeading(item.getHeading());
            onlinePlayerHistory.setDistance(item.getLastDistance());
            onlinePlayerHistory.setUpdateTime(item.getUpdateTime());
            return onlinePlayerHistory;
        }).toList();
        CollUtil.split(onlinePlayerHistoryList, 800).forEach(onlinePlayerHistoryMapper::insertBatch);

        log.info("job::同步在线玩家数据完成 cost={}ms, count={}", timer.intervalRestart(), onlinePlayerList.size());
        if (isError) {
            ThreadUtil.sleep(3 * 1000);
        } else {
            ThreadUtil.sleep(20 * 1000);
        }
    }

    public List<PlayerMileage> playerMileageHandle(List<OnlinePlayer> onlinePlayerList) {
        // 获取所有服务器ID集合
        Set<Integer> serverIdSet = serverService.selectAll().stream().map(Server::getServerId).collect(Collectors.toSet());

        // 查询玩家里程信息
        List<PlayerMileage> playerMileageList = playerMileageService.findByTmpIdList(onlinePlayerList.stream().map(OnlinePlayer::getTmpId).toList());
        Map<Integer, PlayerMileage> playerMileageMap = playerMileageList.stream().collect(Collectors.toMap(PlayerMileage::getTmpId, item -> item));

        // 遍历处理数据
        List<PlayerMileage> newPlayerMileageList = CollUtil.newArrayList();
        onlinePlayerList.parallelStream().forEach(onlinePlayer -> {
            // 获取玩家的里程信息，没有则初始化数据
            PlayerMileage playerMileage = playerMileageMap.get(onlinePlayer.getTmpId());
            if (playerMileage == null) {
                playerMileage = new PlayerMileage();
                playerMileage.setTmpId(onlinePlayer.getTmpId());
                playerMileage.setTmpName(onlinePlayer.getTmpName());
                playerMileage.setServerId(onlinePlayer.getServerId());
                playerMileage.setAxisX(onlinePlayer.getAxisX());
                playerMileage.setAxisY(onlinePlayer.getAxisY());
                playerMileage.setHeading(onlinePlayer.getHeading());
                playerMileage.setLastDistance(0);
                playerMileage.setDistance(0L);
                playerMileage.setTodayDistance(0L);
                playerMileage.setUpdateTime(onlinePlayer.getUpdateTime());
                newPlayerMileageList.add(playerMileage);
                return;
            }

            // 间隔时长和里程
            long intervalSecond = DateUtil.between(playerMileage.getUpdateTime(), onlinePlayer.getUpdateTime(), DateUnit.SECOND);
            int intervalDistance = this.distance(onlinePlayer.getAxisX(), onlinePlayer.getAxisY(), playerMileage.getAxisX(), playerMileage.getAxisY()) * 19;

            // 以下条件不记录里程数据
            // 1.时长间隔大于60秒或服务器变更或间隔距离异常过大
            // 2.服务器不存在或非ETS服务器
            if (intervalSecond > 60 || !onlinePlayer.getServerId().equals(playerMileage.getServerId()) || intervalDistance > 50000) {
                intervalDistance = 0;
            } else if (!serverIdSet.contains(playerMileage.getServerId())) {
                intervalDistance = 0;
            }

            // 更新在线信息
            playerMileage.setTmpName(onlinePlayer.getTmpName());
            playerMileage.setServerId(onlinePlayer.getServerId());
            playerMileage.setHeading(onlinePlayer.getHeading());
            playerMileage.setAxisX(onlinePlayer.getAxisX());
            playerMileage.setAxisY(onlinePlayer.getAxisY());
            playerMileage.setLastDistance(intervalDistance);
            playerMileage.setDistance(playerMileage.getDistance() + intervalDistance);
            playerMileage.setTodayDistance(playerMileage.getTodayDistance() + intervalDistance);
            playerMileage.setUpdateTime(onlinePlayer.getUpdateTime());
        });

        // 保存数据
        if (CollUtil.isNotEmpty(newPlayerMileageList)) {
            CollUtil.split(newPlayerMileageList, 800).forEach(playerMileageService::insertBatch);
        }
        if (MapUtil.isNotEmpty(playerMileageMap)) {
            CollUtil.split(playerMileageMap.values(), 800).forEach(playerMileageService::updateBatch);
        }

        List<PlayerMileage> meragePlayerMileageList = CollUtil.newArrayList(playerMileageMap.values());
        meragePlayerMileageList.addAll(newPlayerMileageList);
        return meragePlayerMileageList;
    }

    /**
     * 计算两点间距离
     */
    public int distance(Integer x1, Integer y1, Integer x2, Integer y2) {
        if (x1 == null || y1 == null || x2 == null || y2 == null) {
            return 0;
        }
        double dx = x2 - x1;
        double dy = y2 - y1;
        return (int) Math.sqrt(dx * dx + dy * dy);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanTodayDistanceJob() {
        log.info("cleanTodayDistanceJob::清理玩家今日里程数据");
        playerMileageService.cleanTodayDistance();
    }
}
