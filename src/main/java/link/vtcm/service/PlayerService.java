package link.vtcm.service;

import link.vtcm.domain.Player;
import link.vtcm.domain.vo.PlayerMileageVO;
import link.vtcm.domain.vo.PlayerVO;

import java.util.List;

/**
 * 玩家
 * @author zhangmj
 * @since 2025/7/2
 */
public interface PlayerService {
    /**
     * 同步数据
     */
    Player sync(Integer tmpId);

    /**
     * 获取玩家信息
     */
    Player getByTmpId(Integer tmpId);

    /**
     * 获取玩家信息
     */
    PlayerVO info(Integer tmpId);

    /**
     * 获取玩家里程信息
     * @param tmpIdList tmpId集合
     */
    List<PlayerMileageVO> mileage(List<Integer> tmpIdList);
}
