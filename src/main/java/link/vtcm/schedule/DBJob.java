package link.vtcm.schedule;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import link.vtcm.mapper.OnlinePlayerHistoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 数据库相关定时任务
 * @author zhangmj
 * @since 2026/1/21
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DBJob {
    private final OnlinePlayerHistoryMapper onlinePlayerHistoryMapper;

    @Scheduled(cron = "0 0 0 * * *")
    public void gameOnlinePlayerHistoryPartitionJob() {
        Date nextDay = DateUtil.offsetDay(DateUtil.date(), 1);
        String partition = "p" + DatePattern.PURE_DATE_FORMAT.format(nextDay);
        String date = DatePattern.NORM_DATETIME_FORMAT.format(DateUtil.offsetDay(nextDay, 1));
        log.info("gameOnlinePlayerHistoryPartitionJob::创建玩家在线历史表分区 p={}, date={}", partition, date);
        onlinePlayerHistoryMapper.createPartition(partition, date);
    }
}
