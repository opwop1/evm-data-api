package link.vtcm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import link.vtcm.domain.PlayerVtcHistory;

/**
 * 玩家VTC历史信息
 * @author zhangmj
 * @since 2025/5/21
 */
public interface PlayerVtcHistoryMapper extends BaseMapper<PlayerVtcHistory> {
    /**
     * 删除
     */
    void deleteByTmpId(Integer tmpId);
}
