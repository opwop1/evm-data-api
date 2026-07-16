package link.vtcm.domain.vo;

import link.vtcm.common.constant.GameConstant;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 地图标点 VO
 * @author zhangmj
 * @since 2025/12/16
 */
@Data
public class MapMarkerVO {
    private String id;

    /**
     * 父级 ID
     */
    private String parentId;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     * @see GameConstant.MapMarkerTypeEnum 标点类型枚举
     */
    private Integer type;

    /**
     * 坐标 X
     */
    private BigDecimal axisX;

    /**
     * 坐标 Y
     */
    private BigDecimal axisY;

    /**
     * 图标地址
     */
    private String iconUrl;
}
