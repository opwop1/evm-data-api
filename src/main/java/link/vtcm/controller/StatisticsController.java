package link.vtcm.controller;

import cn.hutool.core.collection.CollUtil;
import jakarta.validation.Valid;
import link.vtcm.common.exception.BaseException;
import link.vtcm.common.util.R;
import link.vtcm.domain.param.MileageRankingParam;
import link.vtcm.domain.vo.MileageRankingVO;
import link.vtcm.service.PlayerMileageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 统计
 * @author zhangmj
 * @since 2025/8/24
 */
@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final PlayerMileageService playerMileageService;

    /**
     * 玩家里程排行榜
     */
    @GetMapping("/mileageRankingList")
    public R<List<MileageRankingVO>> mileageRankingList(@Valid MileageRankingParam param) {
        // 设置默认排行榜数量
        if (param.getRankingCount() == null) {
            param.setRankingCount(10);
        }
        
        // 如果指定了tmpId，则返回该玩家的排名信息
        if (param.getTmpId() != null) {
            MileageRankingVO playerRanking = playerMileageService.getPlayerRanking(param);
            if (playerRanking == null) {
                throw new BaseException("未找到该玩家的里程数据");
            }
            return R.ok(CollUtil.newArrayList(playerRanking));
        }
        
        // 获取排行榜数据
        List<MileageRankingVO> rankingList = playerMileageService.getMileageRanking(param);
        return R.ok(rankingList);
    }
}
