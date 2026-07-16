package link.vtcm.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import link.vtcm.domain.PlayerMileage;
import link.vtcm.domain.param.MileageRankingParam;
import link.vtcm.domain.vo.MileageRankingVO;
import link.vtcm.mapper.PlayerMileageMapper;
import link.vtcm.service.PlayerMileageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 玩家里程
 * @author zhangmj
 * @since 2025/8/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerMileageServiceImpl implements PlayerMileageService {
    private final PlayerMileageMapper playerMileageMapper;

    @Override
    public List<PlayerMileage> findByTmpIdList(List<Integer> tmpIdList) {
        List<PlayerMileage> playerMileageList = CollUtil.newArrayList();
        CollUtil.split(tmpIdList, 1000).forEach(tmpIdListSplit -> playerMileageList.addAll(playerMileageMapper.selectList(new LambdaQueryWrapper<PlayerMileage>().in(PlayerMileage::getTmpId, tmpIdListSplit))));
        return playerMileageList;
    }

    @Override
    public void insertBatch(List<PlayerMileage> list) {
        playerMileageMapper.insertBatch(list);
    }

    @Override
    public void updateBatch(List<PlayerMileage> list) {
        playerMileageMapper.updateBatch(list);
    }

    @Override
    public void cleanTodayDistance() {
        playerMileageMapper.update(null, new LambdaUpdateWrapper<PlayerMileage>().set(PlayerMileage::getTodayDistance, 0));
    }

    @Override
    public PlayerMileage getByTmpId(Integer tmpId) {
        return playerMileageMapper.selectOne(new LambdaQueryWrapper<PlayerMileage>().eq(PlayerMileage::getTmpId, tmpId));
    }

    @Override
    public List<MileageRankingVO> getMileageRanking(MileageRankingParam param) {
        return playerMileageMapper.selectMileageRanking(param);
    }

    @Override
    public MileageRankingVO getPlayerRanking(MileageRankingParam param) {
        return playerMileageMapper.selectPlayerRanking(param);
    }
}