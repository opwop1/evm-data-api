package link.vtcm.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 玩家里程
 * @author zhangmj
 * @since 2025/8/29
 */
@Data
public class PlayerMileageVO {
    /**
     * TMP编号
     */
    private Integer tmpId;

    /**
     * 玩家名称
     */
    private String tmpName;

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
     * 总里程 (米)
     */
    private Long distance;

    /**
     * 当日里程 (米)
     */
    private Long todayDistance;

    /**
     * 最后在线时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastOnlineTime;
}
