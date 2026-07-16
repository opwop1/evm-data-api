package link.vtcm.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import link.vtcm.api.TruckersMpApi;
import link.vtcm.common.constant.CacheConstant;
import link.vtcm.common.constant.GameConstant;
import link.vtcm.common.constant.LockConstant;
import link.vtcm.common.constant.StatusCodeConstant;
import link.vtcm.common.exception.BaseException;
import link.vtcm.common.util.SyncUtil;
import link.vtcm.domain.Vtc;
import link.vtcm.domain.dto.TmpResult;
import link.vtcm.domain.dto.TmpVtc;
import link.vtcm.domain.dto.TmpVtcMembers;
import link.vtcm.domain.vo.VtcMemberVO;
import link.vtcm.domain.vo.VtcVO;
import link.vtcm.mapper.VtcMapper;
import link.vtcm.service.VtcService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * VTC
 * @author zhangmj
 * @since 2025/7/3
 */
@Service
@RequiredArgsConstructor
public class VtcServiceImpl implements VtcService {
    private final VtcMapper baseMapper;
    private final TruckersMpApi truckersMpApi;
    private final SyncUtil syncUtil;
    private final RedissonClient redissonClient;

    @Override
    public Vtc sync(Integer vtcId) {
        // 查询VTC信息
        TmpResult<TmpVtc> result = null;
        try {
            result = truckersMpApi.vtc(vtcId);
        } catch (ForestRuntimeException e) {
            if (StrUtil.isNotEmpty(e.getMessage()) && e.getMessage().contains("Page not found")) {
                throw new BaseException(StatusCodeConstant.VTC_NOT_EXIST, "VTC 不存在");
            }
            throw new BaseException(e.getMessage());
        }
        if (BooleanUtil.isTrue(result.getError())) {
            throw new BaseException(StrUtil.format("查询VTC信息失败 ({})", result.getDescriptor()));
        }
        TmpVtc vtcResult = result.getResponse();

        // 转换实体类
        Vtc vtc = new Vtc();
        vtc.setVtcId(vtcId);
        vtc.setName(SensitiveWordHelper.replace(vtcResult.getName()));
        vtc.setOwnerTmpId(vtcResult.getOwnerId());
        vtc.setLogoUrl(vtcResult.getLogo());
        vtc.setCoverUrl(vtcResult.getCover());
        vtc.setMemberCount(vtcResult.getMembersCount());
        vtc.setCreateTime(DateUtil.offsetHour(vtcResult.getCreated(), 8));
        vtc.setUpdateTime(DateUtil.date());

        // 认证级别
        if (BooleanUtil.isTrue(vtcResult.getVerified())) {
            vtc.setLevel(GameConstant.VtcLevelEnum.RED.getLevel());
        } else if (BooleanUtil.isTrue(vtcResult.getValidated())) {
            vtc.setLevel(GameConstant.VtcLevelEnum.ORANGE.getLevel());
        } else {
            vtc.setLevel(GameConstant.VtcLevelEnum.NORMAL.getLevel());
        }

        // 保存数据
        syncUtil.wLook(LockConstant.SYNC_VTC + vtc, 3000L, () -> {
            baseMapper.deleteById(vtcId);
            baseMapper.insert(vtc);
            return null;
        });

        return vtc;
    }

    @Override
    public VtcVO info(Integer vtcId) {
        Vtc vtc = this.getByVtcId(vtcId);
        if (vtc == null) {
            throw new BaseException("VTC信息不存在");
        }

        // 转换VO
        VtcVO vo = new VtcVO();
        BeanUtils.copyProperties(vtc, vo);
        return vo;
    }

    @Override
    public Vtc getByVtcId(Integer vtcId) {
        // 从数据库中查询数据
        Vtc vtc = syncUtil.rLook(LockConstant.SYNC_VTC + vtcId, 3000L, () -> baseMapper.selectById(vtcId));
        // 如果数据距离更新时间已经超过24小时，需要重新同步
        if (vtc != null && DateUtil.between(vtc.getUpdateTime(), DateUtil.date(), DateUnit.HOUR) >= 24) {
            vtc = null;
        }

        // 如果数据为空，从第三方同步数据
        if (vtc == null) {
            vtc = this.sync(vtcId);
        }

        return vtc;
    }

    @Override
    public List<VtcMemberVO> memberAll(Integer vtcId) {
        // 尝试从缓存中获取
        String key = CacheConstant.VTC_MEMBER_ALL + vtcId;
        RBucket<List<VtcMemberVO>> bucket = redissonClient.getBucket(key);
        List<VtcMemberVO> voList = bucket.get();
        if (voList != null) {
            return voList;
        }

        // 查询车队信息
        Vtc vtc = this.getByVtcId(vtcId);

        // 查询成员信息
        TmpResult<TmpVtcMembers> result = truckersMpApi.vtcAllMember(vtcId);
        if (BooleanUtil.isTrue(result.getError())) {
            throw new BaseException(StrUtil.format("查询VTC成员失败 ({})", result.getDescriptor()));
        }

        // 转为VO
        voList = result.getResponse().getMembers().stream().map(item -> {
            VtcMemberVO vo = new VtcMemberVO();
            vo.setMemberId(item.getMemberId());
            vo.setTmpId(item.getUserId());
            vo.setIsOwner(BooleanUtil.isTrue(item.getIsOwner()) || vtc.getOwnerTmpId().equals(item.getUserId()));
            vo.setName(SensitiveWordHelper.replace(item.getUsername()));
            vo.setRole(SensitiveWordHelper.replace(item.getRole()));
            vo.setJoinDate(item.getJoinDate());
            return vo;
        }).toList();

        // 写入缓存
        bucket.set(voList, Duration.ofMinutes(10));

        return voList;
    }
}
