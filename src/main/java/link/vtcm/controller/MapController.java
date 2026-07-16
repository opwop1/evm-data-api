package link.vtcm.controller;

import jakarta.validation.Valid;
import link.vtcm.common.util.R;
import link.vtcm.domain.param.MapMarkerParam;
import link.vtcm.domain.param.MapPlayerHistoryParam;
import link.vtcm.domain.param.MapPlayerListParam;
import link.vtcm.domain.vo.MapMarkerVO;
import link.vtcm.domain.vo.MapPlayerHistoryVO;
import link.vtcm.domain.vo.MapPlayerVO;
import link.vtcm.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 地图
 * @author zhangmj
 * @since 2025/7/2
 */
@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/map")
@RequiredArgsConstructor
public class MapController {
    private final MapService mapService;

    /**
     * 玩家列表
     */
    @GetMapping("/playerList")
    public R<List<MapPlayerVO>> playerList(MapPlayerListParam param) {
        return R.ok(mapService.playerList(param));
    }

    /**
     * 地图标点
     */
    @GetMapping("/marker")
    public R<List<MapMarkerVO>> marker(@Valid MapMarkerParam param) {
        return R.ok(mapService.findMarker(param));
    }

    /**
     * 玩家历史数据
     */
    @GetMapping("/playerHistory")
    public R<List<MapPlayerHistoryVO>> playerHistory(MapPlayerHistoryParam param) {
        return R.ok(mapService.playerHistory(param));
    }
}
