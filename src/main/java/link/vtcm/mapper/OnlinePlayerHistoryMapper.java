package link.vtcm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import link.vtcm.domain.OnlinePlayer;
import link.vtcm.domain.OnlinePlayerHistory;

import java.util.List;

/**
 * 玩家在线历史
 * @author zhangmj
 * @since 2026/1/21
 */
public interface OnlinePlayerHistoryMapper extends BaseMapper<OnlinePlayerHistory> {
    /**
     * 创建分区
     * @param partition 分区名
     * @param date 日期
     */
    void createPartition(String partition, String date);

    /**
     * 批量插入
     */
    void insertBatch(List<OnlinePlayerHistory> list);
}
