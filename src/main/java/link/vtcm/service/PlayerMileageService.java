package link.vtcm.service;

import link.vtcm.domain.PlayerMileage;
import link.vtcm.domain.param.MileageRankingParam;
import link.vtcm.domain.vo.MileageRankingVO;

import java.util.List;

/**
 * 玩家里程
 * @author zhangmj
 * @since 2025/8/23
 */
public interface PlayerMileageService {
    /**
     * 根据 tmpId 集合查询
     * @param tmpIdList tmpId集合
     */
    List<PlayerMileage> findByTmpIdList(List<Integer> tmpIdList);

    /**
     * 批量插入
     */
    void insertBatch(List<PlayerMileage> list);

    /**
     * 批量修改
     */
    void updateBatch(List<PlayerMileage> list);

    /**
     * 清理玩家今日里程数据
     */
    void cleanTodayDistance();

    /**
     * 根据 tmpId 查询
     */
    PlayerMileage getByTmpId(Integer tmpId);

    /**
     * 获取玩家里程排行榜
     */
    List<MileageRankingVO> getMileageRanking(MileageRankingParam param);

    /**
     * 获取指定玩家里程排名
     */
    MileageRankingVO getPlayerRanking(MileageRankingParam param);
}