package link.vtcm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 玩家里程
 * @author zhangmj
 * @since 2025/8/23
 */
@Data
@TableName("game_player_mileage")
public class PlayerMileage {
    @TableId
    private Long id;

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
     * 更新时间
     */
    private Date updateTime;

    /**
     * 上一次行驶里程
     */
    @TableField(exist = false)
    private Integer lastDistance;
}
