package link.vtcm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 在线玩家
 * @author zhangmj
 * @since 2025/4/10
 */
@Data
@TableName("game_online_player")
public class OnlinePlayer {
    @TableId(type = IdType.INPUT)
    private Integer tmpId;

    /** TMP名称 */
    private String tmpName;

    /** 服务器玩家ID */
    private Integer serverPlayerId;

    /** 服务器ID */
    private Integer serverId;

    /** 坐标轴X */
    private Integer axisX;

    /** 坐标轴Y */
    private Integer axisY;

    /** 航向 */
    private BigDecimal heading;

    /** 更新时间 */
    private Date updateTime;
}
