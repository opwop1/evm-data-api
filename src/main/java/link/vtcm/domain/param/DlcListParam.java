package link.vtcm.domain.param;

import lombok.Data;
import link.vtcm.common.constant.GameConstant;

/**
 * DLC列表入参封装
 * @author zhangmj
 * @since 2025/6/30
 */
@Data
public class DlcListParam {
    /**
     * 类型
     * @see GameConstant.GameSteamDlcTypeEnum 类型枚举
     */
    private Integer type;
}
