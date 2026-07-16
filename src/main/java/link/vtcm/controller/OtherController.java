package link.vtcm.controller;

import link.vtcm.common.util.R;
import link.vtcm.domain.vo.TmpFileVO;
import link.vtcm.domain.vo.TmpVersionVO;
import link.vtcm.service.OtherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 其他
 * @author zhangmj
 * @since 2026/2/2
 */
@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/other")
@RequiredArgsConstructor
public class OtherController {
    private final OtherService otherService;

    /**
     * TMP 文件信息
     */
//    @GetMapping("/tmpFileList")
    public R<List<TmpFileVO>> tmpFileList() {
        return R.ok(otherService.tmpFileList());
    }

    /**
     * TMP 版本信息
     */
    @GetMapping ("/tmpVersion")
    public R<TmpVersionVO> tmpVersion() {
        return R.ok(otherService.tmpVersion());
    }

    /**
     * 翻译
     */
    @GetMapping("/translation")
    public R<String> translation(Integer sceneType, String text) {
        return R.ok(null, otherService.translation(sceneType, text));
    }
}
