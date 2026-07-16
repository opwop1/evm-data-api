package link.vtcm.service;

import link.vtcm.domain.PlayerVtcHistory;
import link.vtcm.domain.dto.TmpPlayer;

import java.util.List;

/**
 * 玩家VTC历史
 * @author zhangmj
 * @since 2025/7/2
 */
public interface PlayerVtcHistoryService {
    /**
     * 保存
     */
    void save(TmpPlayer playerResult);

    /**
     * 根据tmpId查询
     */
    List<PlayerVtcHistory> findByTmpId(Integer tmpId);
}
