package link.vtcm.schedule;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import link.vtcm.api.TruckersMpApi;
import link.vtcm.common.constant.Constant;
import link.vtcm.common.constant.LockConstant;
import link.vtcm.common.util.SyncUtil;
import link.vtcm.domain.Server;
import link.vtcm.domain.dto.TmpResult;
import link.vtcm.domain.dto.TmpServer;
import link.vtcm.mapper.ServerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 服务器同步定时任务
 * @author zhangmj
 * @since 2025/7/1
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ServerSyncJob {
    private final SyncUtil syncUtil;
    private final TruckersMpApi truckersMpApi;
    private final ServerMapper serverMapper;

    @Scheduled(fixedDelay = 60 * 1000)
    public void job() {
        log.info("job::开始同步服务器数据 time={}", DatePattern.NORM_DATETIME_FORMAT.format(DateUtil.date()));

        // 获取服务器数据
        TmpResult<List<TmpServer>> serverResult = truckersMpApi.servers();
        if (serverResult.getError()) {
            log.warn("syncJob::获取服务器数据失败，接口响应失败");
            return;
        }
        List<TmpServer> tmpServerList = serverResult.getResponse();

        // 构建数据
        Date updateTime = DateUtil.date();
        List<Server> serverList = CollUtil.newArrayList();
        for (TmpServer tmpServer : tmpServerList) {
            // 仅同步欧卡服务器
            if (!"ETS2".equals(tmpServer.getGame())) {
                continue;
            }
            serverList.add(buildServer(tmpServer, updateTime));
        }

        // 保存数据
        syncUtil.wLook(LockConstant.SYNC_SERVER, 3000L, () -> {
            serverMapper.delete(null);
            serverMapper.insertBatch(serverList);
            return null;
        });
    }

    /**
     * 构建服务器数据对象
     */
    private static Server buildServer(TmpServer tmpServer, Date updateTime) {
        Server server = new Server();
        server.setServerId(tmpServer.getMapId());
        server.setServerIp(tmpServer.getIp());
        server.setServerPort(tmpServer.getPort());
        server.setServerName(tmpServer.getName());
        server.setShortName(tmpServer.getShortName());
        server.setIsOnline(tmpServer.getOnline() ? Constant.ENABLE : Constant.DISABLE);
        server.setPlayerCount(tmpServer.getPlayers());
        server.setMaxPlayer(tmpServer.getMaxPlayers());
        server.setQueueCount(tmpServer.getQueue());
        server.setSpeedLimiterEnable(tmpServer.getSpeedLimiter());
        server.setCollisionsEnable(tmpServer.getCollisions() ? Constant.ENABLE : Constant.DISABLE);
        server.setPoliceCarEnable(tmpServer.getPoliceCarsForPlayers() ? Constant.ENABLE : Constant.DISABLE);
        server.setAfkEnable(tmpServer.getAfkEnabled() ? Constant.ENABLE : Constant.DISABLE);
        server.setUpdateTime(updateTime);
        return server;
    }
}
