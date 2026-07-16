package link.vtcm.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 玩家信息
 * @author zhangmj
 * @since 2025/5/13
 */
@Data
public class PlayerVO {
    /**
     * TMP编号
     */
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
     * 总里程 (米)
     */
    private Long mileage;

    /**
     * 今日里程 (米)
     */
    private Long todayMileage;

    /**
     * 最后在线时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastOnlineTime;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
     * 赞助级别颜色，16进制值
     */
    private String sponsorLevelColor;

    /**
     * 隐藏赞助信息 (1:是; 0:否; )
     */
    private Boolean sponsorHide;

    /**
     * VTC历史信息
     */
    private List<VtcHistory> vtcHistory;

    /**
     * VTC历史信息
     */
    @Data
    public static class VtcHistory {
        /**
         * VTC编号
         */
        private Integer vtcId;

        /**
         * VTC名称
         */
        private String vtcName;

        /**
         * 加入日期
         */
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
        private Date joinDate;

        /**
         * 退出日期
         */
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
        private Date quitDate;
    }
}
