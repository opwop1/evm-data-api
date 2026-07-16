package link.vtcm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import link.vtcm.domain.PlayerVtcHistory;
import link.vtcm.domain.dto.TmpPlayer;
import link.vtcm.mapper.PlayerVtcHistoryMapper;
import link.vtcm.service.PlayerVtcHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 玩家VTC历史
 * @author zhangmj
 * @since 2025/7/2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerVtcHistoryServiceImpl implements PlayerVtcHistoryService {
    private final PlayerVtcHistoryMapper baseMapper;

    @Override
    public void save(TmpPlayer playerResult) {
        // 移除玩家VTC历史信息
        baseMapper.deleteByTmpId(playerResult.getId());

        if (CollUtil.isEmpty(playerResult.getVtcHistory())) {
            return;
        }

        // 保存信息
        List<PlayerVtcHistory> entityList = playerResult.getVtcHistory().stream().map(vtcHistory -> {
            PlayerVtcHistory playerVtcHistory = new PlayerVtcHistory();
            playerVtcHistory.setTmpId(playerResult.getId());
            playerVtcHistory.setVtcId(vtcHistory.getId());
            playerVtcHistory.setVtcName(SensitiveWordHelper.replace(vtcHistory.getName()));
            playerVtcHistory.setJoinDate(vtcHistory.getJoinDate() != null ? DateUtil.offsetHour(vtcHistory.getJoinDate(), 8) : null);
            playerVtcHistory.setQuitDate(vtcHistory.getLeftDate() != null ? DateUtil.offsetHour(vtcHistory.getLeftDate(), 8) : null);
            return playerVtcHistory;
        }).toList();
        entityList.forEach(baseMapper::insert);
    }

    @Override
    public List<PlayerVtcHistory> findByTmpId(Integer tmpId) {
        return baseMapper.selectList(new LambdaQueryWrapper<PlayerVtcHistory>().eq(PlayerVtcHistory::getTmpId, tmpId));
    }
}
