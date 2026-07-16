package link.vtcm.service;

import link.vtcm.domain.param.DlcListParam;
import link.vtcm.domain.vo.DlcVO;

import java.util.List;

/**
 * SteamDLC
 * @author zhangmj
 * @since 2025/7/1
 */
public interface SteamDlcService {
    /**
     * DLC列表
     */
    List<DlcVO> list(DlcListParam param);
}
