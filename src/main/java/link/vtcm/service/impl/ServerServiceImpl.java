package link.vtcm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import link.vtcm.common.constant.LockConstant;
import link.vtcm.common.exception.BaseException;
import link.vtcm.common.util.SyncUtil;
import link.vtcm.domain.Server;
import link.vtcm.domain.ServerHistory;
import link.vtcm.domain.param.ServerListParam;
import link.vtcm.domain.vo.ServerVO;
import link.vtcm.mapper.ServerHistoryMapper;
import link.vtcm.mapper.ServerMapper;
import link.vtcm.service.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 服务器
 * @author zhangmj
 * @since 2025/7/1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ServerServiceImpl implements ServerService {
    private final ServerMapper baseMapper;
    private final SyncUtil syncUtil;
    private final ServerHistoryMapper serverHistoryMapper;

    @Override
    public List<ServerVO> list(ServerListParam param) {
        // 查询数据
        List<Server> serverList = syncUtil.rLook(LockConstant.SYNC_SERVER, 3000L, () -> baseMapper.selectList(new LambdaQueryWrapper<Server>().eq(param.getIsOnline() != null, Server::getIsOnline, param.getIsOnline())));
        if (CollUtil.isEmpty(serverList)) {
            return CollUtil.newArrayList();
        }

        // 获取玩家数量历史数据
        List<ServerHistory> serverHistoryList = serverHistoryMapper.findByServerIdListAndTimeRange(serverList.stream().map(Server::getServerId).toList(), DateUtil.offsetDay(DateUtil.date(), -1), null);
        Map<Integer, List<ServerHistory>> serverHistoryMap = serverHistoryList.stream().collect(Collectors.groupingBy(ServerHistory::getServerId));

        // 转为 VO
        return serverList.stream().map(server -> {
            ServerVO vo = new ServerVO();
            BeanUtils.copyProperties(server, vo);
            List<ServerHistory> currentServerHistoryList = serverHistoryMap.get(vo.getServerId());
            if (CollUtil.isNotEmpty(currentServerHistoryList)) {
                vo.setPlayerHistory(currentServerHistoryList.stream().map(serverHistory -> {
                    ServerVO.PlayerHistory playerHistory = new ServerVO.PlayerHistory();
                    BeanUtils.copyProperties(serverHistory, playerHistory);
                    return playerHistory;
                }).toList());
            } else {
                vo.setPlayerHistory(CollUtil.newArrayList());
            }
            return vo;
        }).toList();
    }

    @Override
    public List<Server> selectAll() {
        return syncUtil.rLook(LockConstant.SYNC_SERVER, 3000L, () -> baseMapper.selectList(null));
    }

    @Override
    public ServerVO info(Integer serverId) {
        // 查询数据
        Server server = syncUtil.rLook(LockConstant.SYNC_SERVER, 3000L, () -> {
            return baseMapper.selectById(serverId);
        });

        if (server == null) {
            throw new BaseException("服务器不存在");
        }

        // 转为VO
        ServerVO vo = new ServerVO();
        BeanUtils.copyProperties(server, vo);
        return vo;
    }
}
