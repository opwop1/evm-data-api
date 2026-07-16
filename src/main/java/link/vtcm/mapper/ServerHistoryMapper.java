package link.vtcm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import link.vtcm.domain.ServerHistory;

import java.util.Date;
import java.util.List;

/**
 * 服务器人数历史
 * @author zhangmj
 * @since 2026/5/9
 */
public interface ServerHistoryMapper extends BaseMapper<ServerHistory> {
    void insertBatch(List<ServerHistory> list);

    /**
     * 根据服务器 ID 查询
     */
    List<ServerHistory> findByServerIdListAndTimeRange(List<Integer> serverIdList, Date startTime, Date endTime);
}
