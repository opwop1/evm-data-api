package link.vtcm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 玩家信息
 * @author zhangmj
 * @since 2025/5/13
 */
@Data
@TableName("game_player")
public class Player {
    /**
     * TMP编号
     */
    @TableId(type = IdType.INPUT)
    private Integer tmpId;

    /**
     * 玩家名称
     */
    private String name;

    /**
     * 头像地址
     */
    private String avatarUrl;

    /**
     * 小尺寸头像地址
     */
    private String smallAvatarUrl;

    /**
     * 注册时间
     */
    private Date registerTime;

    /**
     * Steam ID
     */
    private String steamId;

    /**
     * 用户组名称
     */
    private String groupName;

    /**
     * 用户组颜色，16进制值
     */
    private String groupColor;

    /**
     * 是否加入VTC (1:是; 0:否; )
     */
    private Boolean isJoinVtc;

    /**
     * VTC成员ID
     */
    private Integer vtcMemberId;

    /**
     * VTC编号
     */
    private Integer vtcId;

    /**
     * VTC名称
     */
    private String vtcName;

    /**
     * VTC角色
     */
    private String vtcRole;

    /**
     * VTC加入时间
     */
    private Date vtcJoinTime;

    /**
     * 是否封禁 (1:是; 0:否; )
     */
    private Boolean isBan;

    /**
     * 封禁截止时间
     */
    private Date banUntil;

    /**
     * 封禁次数
     */
    private Integer banCount;

    /**
     * 封禁原因
     */
    private String banReason;

    /**
     * 封禁原因，中文
     */
    private String banReasonZh;

    /**
     * 隐藏封禁信息 (1:是; 0:否; )
     */
    private Boolean banHide;

    /**
     * 是否赞助 (1:是; 0:否; )
     */
    private Boolean isSponsor;

    /**
     * 赞助金额 (美分)
     */
    private Integer sponsorAmount;

    /**
     * 累计赞助金额 (美分)
     */
    private Integer sponsorCumulativeAmount;

    /**
     * 赞助级别颜色
     */
    private String sponsorLevelColor;

    /**
     * 隐藏赞助信息 (1:是; 0:否; )
     */
    private Boolean sponsorHide;

    /**
     * 更新时间
     */
    private Date updateTime;
}
