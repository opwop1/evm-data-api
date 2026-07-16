package link.vtcm.service;

import link.vtcm.domain.Vtc;
import link.vtcm.domain.vo.VtcMemberVO;
import link.vtcm.domain.vo.VtcVO;

import java.util.List;

/**
 * VTC
 * @author zhangmj
 * @since 2025/7/3
 */
public interface VtcService {
    /**
     * 同步数据
     */
    Vtc sync(Integer vtcId);

    /**
     * 获取VTC信息
     */
    VtcVO info(Integer vtcId);

    /**
     * 获取VTC信息
     */
    Vtc getByVtcId(Integer vtcId);

    /**
     * 获取VTC成员列表
     */
    List<VtcMemberVO> memberAll(Integer vtcId);
}
