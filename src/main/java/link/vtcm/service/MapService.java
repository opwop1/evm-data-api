package link.vtcm.service;

import jakarta.validation.Valid;
import link.vtcm.domain.param.MapMarkerParam;
import link.vtcm.domain.param.MapPlayerHistoryParam;
import link.vtcm.domain.param.MapPlayerListParam;
import link.vtcm.domain.vo.MapMarkerVO;
import link.vtcm.domain.vo.MapPlayerHistoryVO;
import link.vtcm.domain.vo.MapPlayerVO;

import java.util.List;

/**
 * 地图
 * @author zhangmj
 * @since 2025/7/2
 */
public interface MapService {
    /**
     * 玩家列表
     */
    List<MapPlayerVO> playerList(MapPlayerListParam param);

    /**
     * 地图标点
     */
    List<MapMarkerVO> findMarker(MapMarkerParam param);

    /**
     * 玩家历史数据
     */
    List<MapPlayerHistoryVO> playerHistory(MapPlayerHistoryParam param);
}
