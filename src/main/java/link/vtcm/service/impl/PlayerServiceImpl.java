package link.vtcm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import link.vtcm.api.BaiduTranslateApi;
import link.vtcm.api.TruckersMpApi;
import link.vtcm.common.constant.LockConstant;
import link.vtcm.common.constant.StatusCodeConstant;
import link.vtcm.common.exception.BaseException;
import link.vtcm.common.util.SyncUtil;
import link.vtcm.common.util.TransUtil;
import link.vtcm.domain.Player;
import link.vtcm.domain.PlayerMileage;
import link.vtcm.domain.PlayerVtcHistory;
import link.vtcm.domain.dto.*;
import link.vtcm.domain.vo.PlayerMileageVO;
import link.vtcm.domain.vo.PlayerVO;
import link.vtcm.mapper.PlayerMapper;
import link.vtcm.mapper.PlayerMileageMapper;
import link.vtcm.service.PlayerMileageService;
import link.vtcm.service.PlayerService;
import link.vtcm.service.PlayerVtcHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 玩家
 * @author zhangmj
 * @since 2025/7/2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private final PlayerMapper baseMapper;
    private final SyncUtil syncUtil;
    private final TruckersMpApi truckersMpApi;
    private final PlayerVtcHistoryService playerVtcHistoryService;
    private final PlayerMileageService playerMileageService;
    private final TransUtil transUtil;

    @Override
    public Player getByTmpId(Integer tmpId) {
        return syncUtil.wLook(LockConstant.SYNC_PLAYER + tmpId, 3000L, () -> {
            // 从数据库中查询数据
            Player player = baseMapper.selectById(tmpId);
            // 如果数据距离更新时间已经超过30分钟，需要重新同步
            if (player != null && DateUtil.between(player.getUpdateTime(), DateUtil.date(), DateUnit.MINUTE) > 30) {
                player = null;
            }

            // 如果数据为空，从第三方同步数据
            if (player == null) {
                player = this.sync(tmpId);
            }

            return player;
        });
    }

    @Override
    public Player sync(Integer tmpId) {
        // 查询玩家信息
        TmpResult<TmpPlayer> result = null;
        try {
            result = truckersMpApi.player(tmpId);
        } catch (ForestRuntimeException e) {
            if (StrUtil.isNotEmpty(e.getMessage()) && e.getMessage().contains("Unable to find player with that ID")) {
                throw new BaseException(StatusCodeConstant.PLAYER_NOT_EXIST, "玩家不存在");
            }
            throw new BaseException(e.getMessage());
        }
        if (BooleanUtil.isTrue(result.getError())) {
            throw new BaseException(StrUtil.format("查询玩家信息失败 ({})", result.getDescriptor()));
        }

        // 转换实体类
        TmpPlayer playerResult = result.getResponse();
        Player player = new Player();
        player.setTmpId(tmpId);
        player.setName(SensitiveWordHelper.replace(playerResult.getName()));
        player.setAvatarUrl(playerResult.getAvatar());
        player.setSmallAvatarUrl(playerResult.getSmallAvatar());
        player.setRegisterTime(DateUtil.offsetHour(playerResult.getJoinDate(), 8));
        player.setSteamId(playerResult.getSteamId());
        player.setGroupName(playerResult.getGroupName());
        player.setGroupColor(StrUtil.removeAll(playerResult.getGroupColor(), StringPool.HASH));
        player.setIsJoinVtc(playerResult.getVtc().getInVtc());
        player.setIsBan(playerResult.getBanned());
        player.setBanUntil(DateUtil.offsetHour(playerResult.getBannedUntil(), 8));
        player.setBanCount(playerResult.getBansCount());
        player.setBanHide(!playerResult.getDisplayBans());
        player.setIsSponsor(playerResult.getPatreon().getActive());
        player.setSponsorAmount(playerResult.getPatreon().getCurrentPledge());
        player.setSponsorCumulativeAmount(playerResult.getPatreon().getLifetimePledge());
        player.setSponsorLevelColor(StrUtil.removeAll(playerResult.getPatreon().getColor(), StringPool.HASH));
        player.setSponsorHide(playerResult.getPatreon().getHidden());
        player.setUpdateTime(DateUtil.date());

        // 补充封禁信息
        this.fillPlayerBan(player);

        // 补充车队信息
        if (player.getIsJoinVtc()) {
            player.setVtcId(playerResult.getVtc().getId());
            player.setVtcMemberId(playerResult.getVtc().getMemberId());
            player.setVtcName(SensitiveWordHelper.replace(playerResult.getVtc().getName()));
            this.fillPlayerVtc(player, playerResult.getVtc().getMemberId());
        }

        // 保存玩家信息
        baseMapper.deleteById(tmpId);
        baseMapper.insert(player);

        // 保存VTC历史信息
        playerVtcHistoryService.save(playerResult);

        return player;
    }

    /**
     * 补充封禁信息
     */
    private void fillPlayerBan(Player player) {
        // 未封禁，不处理封禁信息
        if (!player.getIsBan()) {
            return;
        }

        try {
            // 查询封禁信息
            TmpResult<List<TmpBans>> bans = truckersMpApi.bans(player.getTmpId());
            if (bans.getError() || CollUtil.isEmpty(bans.getResponse())) {
                return;
            }

            // 补充封禁信息
            TmpBans ban = CollUtil.getFirst(bans.getResponse());
            player.setBanUntil(DateUtil.offsetHour(ban.getExpiration(), 8));
            player.setBanReason(ban.getReason());

            // 翻译封禁原因
            if (StrUtil.isNotBlank(player.getBanReason())) {
                player.setBanReasonZh(transUtil.trans(player.getBanReason(), true));
            }
        } catch (Exception e) {
            log.warn("fillPlayerBan::获取封禁信息失败", e);
        }
    }

    /**
     * 补充VTC信息
     */
    private void fillPlayerVtc(Player player, Integer vtcMemberId) {
        // 未加入VTC，不处理VTC信息
        if (!player.getIsJoinVtc()) {
            return;
        }

        try {
            // 查询VTC成员信息
            TmpResult<TmpVtcMember> vtcMemberResult = truckersMpApi.vtcMember(player.getVtcId(), vtcMemberId);
            if (vtcMemberResult.getError()) {
                return;
            }

            TmpVtcMember vtcMember = vtcMemberResult.getResponse();
            player.setVtcJoinTime(DateUtil.offsetHour(vtcMember.getJoinDate(), 8));
            player.setVtcRole(SensitiveWordHelper.replace(vtcMember.getRole()));
        } catch (Exception e) {
            log.warn("fillPlayerVtc::获取VTC成员信息失败", e);
        }
    }

    @Override
    public PlayerVO info(Integer tmpId) {
        // 获取玩家信息
        Player player = this.getByTmpId(tmpId);
        if (player == null) {
            throw new BaseException("玩家信息不存在");
        }

        // 查询VTC历史信息
        List<PlayerVtcHistory> vtcHistoryList = playerVtcHistoryService.findByTmpId(tmpId);
        List<PlayerVO.VtcHistory> vtcHistoryVoList = vtcHistoryList.stream().map(item -> {
            PlayerVO.VtcHistory vtcHistory = new PlayerVO.VtcHistory();
            BeanUtils.copyProperties(item, vtcHistory);
            return vtcHistory;
        }).toList();

        // 转换VO
        PlayerVO vo = new PlayerVO();
        BeanUtils.copyProperties(player, vo);
        vo.setVtcHistory(vtcHistoryVoList);

        // 查询玩家里程信息
        PlayerMileage playerMileage = playerMileageService.getByTmpId(vo.getTmpId());
        if (playerMileage != null) {
            vo.setMileage(playerMileage.getDistance());
            vo.setTodayMileage(playerMileage.getTodayDistance());
            vo.setLastOnlineTime(playerMileage.getUpdateTime());
        } else {
            vo.setMileage(0L);
            vo.setTodayMileage(0L);
        }

        return vo;
    }

    @Override
    public List<PlayerMileageVO> mileage(List<Integer> tmpIdList) {
        // 获取里程信息集合，转为VO
        List<PlayerMileage> playerMileageList = playerMileageService.findByTmpIdList(tmpIdList);
        return playerMileageList.stream().map(item -> {
            PlayerMileageVO vo = new PlayerMileageVO();
            BeanUtils.copyProperties(item, vo);
            vo.setLastOnlineTime(item.getUpdateTime());
            return vo;
        }).collect(Collectors.toList());
    }
}
