package link.vtcm.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 服务器VO
 * @author zhangmj
 * @since 2025/7/1
 */
@Data
public class ServerVO {
    /**
     * 服务器 ID
     */
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
     * 玩家数量历史
     */
    private List<PlayerHistory> playerHistory;

    @Data
    public static class PlayerHistory {
        /** 玩家数量 */
        private Integer playerCount;

        /** 更新时间 */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
        private Date updateTime;
    }
}
