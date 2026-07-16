package link.vtcm.schedule;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import link.vtcm.api.TruckersMpApi;
import link.vtcm.domain.ServerHistory;
import link.vtcm.domain.dto.TmpResult;
import link.vtcm.domain.dto.TmpServer;
import link.vtcm.mapper.ServerHistoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 服务器历史人物统计任务
 * @author zhangmj
 * @since 2026/5/9
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ServerHistoryJob {
    private final ServerHistoryMapper serverHistoryMapper;
    private final TruckersMpApi truckersMpApi;

    @Scheduled(cron = "0 0/5 * * * *")
    public void job() {
        Date now = DateUtil.date();
        log.info("job::服务器历史数据统计开始执行 time={}", DateUtil.format(now, "yyyy-MM-dd HH:mm"));

        // 获取服务器数据
        TmpResult<List<TmpServer>> serverResult = truckersMpApi.servers();
        if (serverResult.getError()) {
            log.warn("syncJob::获取服务器数据失败，接口响应失败");
            return;
        }
        List<TmpServer> tmpServerList = serverResult.getResponse();

        // 构建数据
        List<ServerHistory> serverHistoryList = CollUtil.newArrayList();
        for (TmpServer tmpServer : tmpServerList) {
            ServerHistory serverHistory = new ServerHistory();
            serverHistory.setServerId(tmpServer.getMapId());
            serverHistory.setPlayerCount(tmpServer.getPlayers());
            serverHistory.setUpdateTime(now);
            serverHistoryList.add(serverHistory);
        }

        // 保存数据
        if (CollUtil.isNotEmpty(serverHistoryList)) {
            serverHistoryMapper.insertBatch(serverHistoryList);
        }
    }
}
