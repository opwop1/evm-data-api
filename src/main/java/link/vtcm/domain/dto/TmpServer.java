package link.vtcm.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * TMP服务器信息
 * @author zhangmj
 * @since 2025/3/31
 */
@Data
public class TmpServer {
    private Integer id;

    /**
     * 游戏
     */
    private String game;

    /**
     * ip
     */
    private String ip;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 服务器名称
     */
    private String name;

    /**
     * 短名称
     */
    @JsonProperty("shortname")
    private String shortName;

    /**
     * 在线
     */
    private Boolean online;

    /**
     * 地图ID
     */
    @JsonProperty("mapid")
    private Integer mapId;

    /**
     * 玩家数量
     */
    private Integer players;

    /**
     * 队列玩家数量
     */
    private Integer queue;

    /**
     * 最大玩家数量
     */
    @JsonProperty("maxplayers")
    private Integer maxPlayers;

    /**
     * 开启限速 (1:是; 0:否; )
     */
    @JsonProperty("speedlimiter")
    private Integer speedLimiter;

    /**
     * 开启碰撞
     */
    private Boolean collisions;

    /**
     * 开启警车
     */
    @JsonProperty("policecarsforplayers")
    private Boolean policeCarsForPlayers;

    /**
     * 开启挂机监测
     */
    @JsonProperty("afkenabled")
    private Boolean afkEnabled;
}
