package link.vtcm.domain.vo;

import link.vtcm.common.constant.GameConstant;
import lombok.Data;

/**
 * DLC信息
 * @author zhangmj
 * @since 2025/6/30
 */
@Data
public class DlcVO {
    /**
     * Steam App Id
     */
    private Integer appId;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     * @see GameConstant.GameSteamDlcTypeEnum 类型枚举
     */
    private Integer type;

    /**
     * 说明
     */
    private String desc;

    /**
     * 标题图片地址
     */
    private String headerImageUrl;

    /**
     * 背景图片地址
     */
    private String backgroundImageUrl;

    /**
     * 原价 (分)
     */
    private Integer originalPrice;

    /**
     * 现价 (分)
     */
    private Integer finalPrice;

    /**
     * 折扣百分比
     */
    private Integer discount;
}
