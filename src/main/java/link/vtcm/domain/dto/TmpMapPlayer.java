package link.vtcm.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * TMP地图玩家
 * @author zhangmj
 * @since 2025/4/11
 */
@Data
public class TmpMapPlayer {
    /**
     * TMP ID
     */
    @JsonProperty("MpId")
    private Integer mpId;

    /**
     * TMP名称
     */
    @JsonProperty("Name")
    private String name;

    /**
     * 服务器玩家ID
     */
    @JsonProperty("PlayerId")
    private Integer playerId;

    /**
     * 服务器ID
     */
    @JsonProperty("ServerId")
    private Integer serverId;

    /**
     * 坐标轴X
     */
    @JsonProperty("X")
    private Integer x;

    /**
     * 坐标轴Y
     */
    @JsonProperty("Y")
    private Integer y;

    /**
     * 航向
     */
    @JsonProperty("Heading")
    private BigDecimal heading;

    /**
     * 更新时间
     */
    @JsonProperty("Time")
    private Long time;
}
