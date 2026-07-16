package link.vtcm.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import link.vtcm.common.exception.BaseException;
import link.vtcm.common.util.R;
import link.vtcm.domain.vo.PlayerMileageVO;
import link.vtcm.domain.vo.PlayerVO;
import link.vtcm.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 玩家
 * @author zhangmj
 * @since 2025/7/2
 */
@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/player")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    /**
     * 玩家信息
     */
    @GetMapping("/info")
    public R<PlayerVO> info(Integer tmpId) {
        if (tmpId == null) {
            throw new BaseException("[tmpId]不能为空");
        }
        return R.ok(playerService.info(tmpId));
    }

    /**
     * 玩家里程
     */
    @GetMapping("/mileage")
    public R<List<PlayerMileageVO>> mileage(String tmpIdList) {
        if (StrUtil.isBlank(tmpIdList)) {
            return R.ok(CollUtil.newArrayList());
        }

        // 入参转换
        List<Integer> tmpIdIntList = CollUtil.newArrayList();
        for (String tmpIdStr : tmpIdList.split(",")) {
            if (StrUtil.isNumeric(tmpIdStr)) {
                tmpIdIntList.add(Integer.parseInt(tmpIdStr));
            }
        }

        return R.ok(playerService.mileage(tmpIdIntList));
    }
}
