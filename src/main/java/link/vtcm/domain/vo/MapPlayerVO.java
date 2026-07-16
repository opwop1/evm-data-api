package link.vtcm.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 地图玩家
 * @author zhangmj
 * @since 2025/4/29
 */
@Data
public class MapPlayerVO {
    /**
     * TMP ID
     */
    private Integer tmpId;

    /**
     * TMP名称
     */
    private String tmpName;

    /**
     * VTC ID
     */
    private Integer vtcId;

    /**
     * 服务器玩家ID
     */
    private Integer serverPlayerId;

    /**
     * 服务器ID
     */
    private Integer serverId;

    /**
     * 坐标轴X
     */
    private Integer axisX;

    /**
     * 坐标轴Y
     */
    private Integer axisY;

    /**
     * 航向
     */
    private BigDecimal heading;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
