package link.vtcm.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import link.vtcm.common.constant.LockConstant;
import link.vtcm.common.util.SyncUtil;
import link.vtcm.domain.MapMarker;
import link.vtcm.domain.OnlinePlayer;
import link.vtcm.domain.OnlinePlayerHistory;
import link.vtcm.domain.param.MapMarkerParam;
import link.vtcm.domain.param.MapPlayerHistoryParam;
import link.vtcm.domain.param.MapPlayerListParam;
import link.vtcm.domain.vo.MapMarkerVO;
import link.vtcm.domain.vo.MapPlayerHistoryVO;
import link.vtcm.domain.vo.MapPlayerVO;
import link.vtcm.mapper.MapMarkerMapper;
import link.vtcm.mapper.OnlinePlayerHistoryMapper;
import link.vtcm.mapper.OnlinePlayerMapper;
import link.vtcm.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 地图
 * @author zhangmj
 * @since 2025/7/2
 */
@Service
@RequiredArgsConstructor
public class MapServiceImpl implements MapService {
    private final OnlinePlayerMapper onlinePlayerMapper;
    private final SyncUtil syncUtil;
    private final MapMarkerMapper mapMarkerMapper;
    private final OnlinePlayerHistoryMapper onlinePlayerHistoryMapper;

    @Override
    public List<MapPlayerVO> playerList(MapPlayerListParam param) {
        // 查询数据
        List<OnlinePlayer> onlinePlayerList = syncUtil.rLook(LockConstant.SYNC_ONLINE_PLAYER, 3000L, () -> onlinePlayerMapper.selectList(new LambdaQueryWrapper<OnlinePlayer>()
            .eq(param.getTmpId() != null, OnlinePlayer::getTmpId, param.getTmpId())
            .in(StrUtil.isNotBlank(param.getTmpIdList()), OnlinePlayer::getTmpId, StrUtil.split(param.getTmpIdList(), StringPool.COMMA))
            .eq(param.getServerId() != null, OnlinePlayer::getServerId, param.getServerId())
            .like(StrUtil.isNotBlank(param.getTmpName()), OnlinePlayer::getTmpName, param.getTmpName())
            .ge(param.getAAxisX() != null, OnlinePlayer::getAxisX, param.getAAxisX())
            .le(param.getAAxisY() != null, OnlinePlayer::getAxisY, param.getAAxisY())
            .le(param.getBAxisX() != null, OnlinePlayer::getAxisX, param.getBAxisX())
            .ge(param.getBAxisY() != null, OnlinePlayer::getAxisY, param.getBAxisY())));

        // 转为VO
        return onlinePlayerList.stream().map(item -> {
            MapPlayerVO vo = new MapPlayerVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).toList();
    }

    @Override
    public List<MapMarkerVO> findMarker(MapMarkerParam param) {
        // 查询标点数据
        List<MapMarker> mapMarkerList = mapMarkerMapper.selectList(new LambdaQueryWrapper<MapMarker>()
            .eq(MapMarker::getMapType, param.getMapType())
            .eq(param.getType() != null, MapMarker::getType, param.getType())
            .ge(param.getAAxisX() != null, MapMarker::getAxisX, param.getAAxisX())
            .ge(param.getAAxisY() != null, MapMarker::getAxisY, param.getAAxisY())
            .le(param.getBAxisX() != null, MapMarker::getAxisX, param.getBAxisX())
            .le(param.getBAxisY() != null, MapMarker::getAxisY, param.getBAxisY())
            .like(StrUtil.isNotBlank(param.getName()), MapMarker::getName, param.getName())
            .eq(param.getParentId() != null, MapMarker::getParentId, param.getParentId()));

        // 转为 VO 并返回
        return mapMarkerList.stream().map(mapMarker -> {
            MapMarkerVO vo = new MapMarkerVO();
            vo.setId(mapMarker.getId().toString());
            vo.setParentId(mapMarker.getParentId().toString());
            vo.setName(mapMarker.getName());
            vo.setType(mapMarker.getType());
            vo.setAxisX(mapMarker.getAxisX());
            vo.setAxisY(mapMarker.getAxisY());
            vo.setIconUrl(mapMarker.getIconUrl());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<MapPlayerHistoryVO> playerHistory(MapPlayerHistoryParam param) {
        List<OnlinePlayerHistory> list = onlinePlayerHistoryMapper.selectList(new LambdaQueryWrapper<OnlinePlayerHistory>()
            .eq(param.getTmpId() != null, OnlinePlayerHistory::getTmpId, param.getTmpId())
            .eq(param.getServerId() != null, OnlinePlayerHistory::getServerId, param.getServerId())
            .ge(param.getStartTime() != null, OnlinePlayerHistory::getUpdateTime, param.getStartTime())
            .le(param.getEndTime() != null, OnlinePlayerHistory::getUpdateTime, param.getEndTime())
            .orderByAsc(OnlinePlayerHistory::getUpdateTime)
            .last("limit 100000"));
        return list.parallelStream().map(item -> {
            MapPlayerHistoryVO vo = new MapPlayerHistoryVO();
            vo.setTmpId(item.getTmpId());
            vo.setServerId(item.getServerId());
            vo.setAxisX(item.getAxisX());
            vo.setAxisY(item.getAxisY());
            vo.setHeading(item.getHeading());
            vo.setUpdateTime(item.getUpdateTime());
            return vo;
        }).toList();
    }
}
