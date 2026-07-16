package link.vtcm.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 玩家在线历史
 * @author zhangmj
 * @since 2026/1/21
 */
@Data
@TableName("game_online_player_history")
public class OnlinePlayerHistory {
    private Integer tmpId;

    /** 服务器ID */
    private Integer serverId;

    /** 坐标轴X */
    private Integer axisX;

    /** 坐标轴Y */
    private Integer axisY;

    /** 航向 */
    private BigDecimal heading;

    /** 行驶里程 (米) */
    private Integer distance;

    /** 更新时间 */
    private Date updateTime;
}
