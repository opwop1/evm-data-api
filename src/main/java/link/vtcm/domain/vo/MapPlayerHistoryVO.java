package link.vtcm.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 玩家历史数据VO
 * @author zhangmj
 * @since 2026/1/22
 */
@Data
public class MapPlayerHistoryVO {
    /**
     * TMP ID
     */
    private Integer tmpId;

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
