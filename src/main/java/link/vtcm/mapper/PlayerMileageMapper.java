package link.vtcm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import link.vtcm.domain.PlayerMileage;
import link.vtcm.domain.param.MileageRankingParam;
import link.vtcm.domain.vo.MileageRankingVO;

import java.util.List;

/**
 * 玩家里程
 * @author zhangmj
 * @since 2025/8/23
 */
public interface PlayerMileageMapper extends BaseMapper<PlayerMileage> {
    /**
     * 批量插入
     */
    void insertBatch(List<PlayerMileage> list);

    /**
     * 批量更新
     */
    void updateBatch(List<PlayerMileage> list);

    /**
     * 获取里程排行榜
     */
    List<MileageRankingVO> selectMileageRanking(MileageRankingParam param);

    /**
     * 获取指定玩家里程排名
     */
    MileageRankingVO selectPlayerRanking(MileageRankingParam param);
}