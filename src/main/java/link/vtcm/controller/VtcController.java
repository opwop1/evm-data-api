package link.vtcm.controller;

import jakarta.validation.constraints.NotNull;
import link.vtcm.common.util.R;
import link.vtcm.domain.vo.VtcMemberVO;
import link.vtcm.domain.vo.VtcVO;
import link.vtcm.service.VtcService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * VTC
 * @author zhangmj
 * @since 2025/7/3
 */
@CrossOrigin(originPatterns = "*")
@Validated
@RestController
@RequestMapping("/vtc")
@RequiredArgsConstructor
public class VtcController {
    private final VtcService vtcService;

    /**
     * VTC信息
     */
    @GetMapping("/info")
    public R<VtcVO> info(@NotNull(message = "[vtcId]不能为空") Integer vtcId) {
        return R.ok(vtcService.info(vtcId));
    }

    /**
     * 成员列表
     */
    @GetMapping("/memberAll")
    public R<List<VtcMemberVO>> memberAll(@NotNull(message = "[vtcId]不能为空") Integer vtcId) {
        return R.ok(vtcService.memberAll(vtcId));
    }
}
