package link.vtcm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 服务器
 * @author zhangmj
 * @since 2025/4/28
 */
@Data
@TableName("game_server")
public class Server {
    /**
     * 服务器ID
     */
    @TableId(type = IdType.INPUT)
    private Integer serverId;

    /**
     * IP
     */
    private String serverIp;

    /**
     * 端口
     */
    private Integer serverPort;

    /**
     * 名称
     */
    private String serverName;

    /**
     * 短名称
     */
    private String shortName;

    /**
     * 是否在线 (1:是; 0:否; )
     */
    private Integer isOnline;

    /**
     * 玩家数量
     */
    private Integer playerCount;

    /**
     * 排队玩家数量
     */
    private Integer queueCount;

    /**
     * 最大玩家数量
     */
    private Integer maxPlayer;

    /**
     * 开启限速 (1:是; 0:否; )
     */
    private Integer speedLimiterEnable;

    /**
     * 开启碰撞 (1:是; 0:否; )
     */
    private Integer collisionsEnable;

    /**
     * 开启警车 (1:是; 0:否; )
     */
    private Integer policeCarEnable;

    /**
     * 开启挂机检测 (1:是; 0:否; )
     */
    private Integer afkEnable;

    /**
     * 更新时间
     */
    private Date updateTime;
}
