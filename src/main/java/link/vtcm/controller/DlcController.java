package link.vtcm.controller;

import link.vtcm.common.util.R;
import link.vtcm.domain.param.DlcListParam;
import link.vtcm.domain.vo.DlcVO;
import link.vtcm.service.SteamDlcService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * DLC
 * @author zhangmj
 * @since 2025/7/1
 */
@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/dlc")
@RequiredArgsConstructor
public class DlcController {
    private final SteamDlcService steamDlcService;

    /**
     * DLC列表
     */
    @GetMapping("/list")
    public R<List<DlcVO>> list(DlcListParam param) {
        return R.ok(steamDlcService.list(param));
    }
}
