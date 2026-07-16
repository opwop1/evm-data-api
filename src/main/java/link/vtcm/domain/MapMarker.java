package link.vtcm.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import link.vtcm.common.constant.GameConstant;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 地图标点
 * @author zhangmj
 * @since 2025/12/16
 */
@Data
@TableName("map_marker")
public class MapMarker {
    @TableId
    private Long id;

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
    private Integer mapType;

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
