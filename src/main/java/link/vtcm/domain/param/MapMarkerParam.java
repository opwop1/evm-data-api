package link.vtcm.domain.param;

import jakarta.validation.constraints.NotNull;
import link.vtcm.common.constant.GameConstant;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 地图标点入参
 * @author zhangmj
 * @since 2025/12/16
 */
@Data
public class MapMarkerParam {
    /**
     * 父级 ID
     */
    private Long parentId;

    /**
     * 名称
     */
    private String name;

    /**
     * 地图类型
     * @see GameConstant.MapMarkerMapTypeEnum 地图类型枚举
     */
    @NotNull(message = "地图类型不能为空")
    private Integer mapType;

    /**
     * 类型
     * @see GameConstant.MapMarkerTypeEnum 标点类型枚举
     */
    private Integer type;

    /**
     * A坐标X轴
     */
    private BigDecimal aAxisX;

    /**
     * A坐标Y轴
     */
    private BigDecimal aAxisY;

    /**
     * B坐标X轴
     */
    private BigDecimal bAxisX;

    /**
     * B坐标Y轴
     */
    private BigDecimal bAxisY;
}
