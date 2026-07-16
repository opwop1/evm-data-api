package link.vtcm.service;

import link.vtcm.domain.vo.TmpFileVO;
import link.vtcm.domain.vo.TmpVersionVO;

import java.util.List;

/**
 * 其他
 * @author zhangmj
 * @since 2026/2/2
 */
public interface OtherService {
    /**
     * TMP 文件信息
     */
    List<TmpFileVO> tmpFileList();

    /**
     * TMP 版本信息
     */
    TmpVersionVO tmpVersion();

    /**
     * 翻译
     */
    String translation(Integer sceneType, String text);
}
