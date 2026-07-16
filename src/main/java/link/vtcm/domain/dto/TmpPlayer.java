package link.vtcm.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * TMP玩家信息
 * @author zhangmj
 * @since 2025/1/3
 */
@Data
public class TmpPlayer {
    /**
     * TMP编号
     */
    private Integer id;

    /**
     * TMP名称
     */
    private String name;

    /**
     * TMP头像地址
     */
    private String avatar;

    /**
     * TMP头像地址
     */
    private String smallAvatar;

    /**
     * TMP注册日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date joinDate;

    /**
     * Steam Id
     */
    @JsonProperty("steamID")
    private String steamId;

    /**
     * 是否封禁
     */
    private Boolean banned;

    /**
     * 封禁截至日期 (banned 为 true 且 bannedUntil 为 null 代表永久封禁)
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date bannedUntil;

    /**
     * 年内封禁次数
     */
    private Integer bansCount;

    /**
     * 显示封禁信息
     */
    private Boolean displayBans;

    /**
     * 用户组名称
     */
    private String groupName;

    /**
     * 用户组颜色
     */
    private String groupColor;

    /**
     * VTC信息
     */
    private PlayerVtc vtc;

    /**
     * VTC历史信息
     */
    private List<PlayerVtcHistory> vtcHistory;

    /**
     * 赞助信息
     */
    private PlayerPatreon patreon;

    /**
     * VTC信息
     */
    @Data
    public static class PlayerVtc {
        /**
         * VTC ID
         */
        private Integer id;

        /**
         * VTC名称
         */
        private String name;

        /**
         * VTC标签
         */
        private String tag;

        /**
         * 是否在VTC
         */
        @JsonProperty("inVTC")
        private Boolean inVtc;

        /**
         * VTC成员ID
         */
        @JsonProperty("memberID")
        private Integer memberId;
    }

    /**
     * 赞助信息
     */
    @Data
    public static class PlayerPatreon {
        /**
         * 赞助过或存在有效赞助
         */
        private Boolean isPatron;

        /**
         * 有效赞助
         */
        private Boolean active;

        /**
         * 赞助级别颜色
         */
        private String color;

        /**
         * 当前赞助金额 (美分)
         */
        private Integer currentPledge;

        /**
         * 累计赞助金额 (美分)
         */
        private Integer lifetimePledge;

        /**
         * 隐藏赞助信息
         */
        private Boolean hidden;
    }

    /**
     * VTC历史信息
     */
    @Data
    public static class PlayerVtcHistory {
        /**
         * VTC成员ID
         */
        private Integer id;

        /**
         * VTC名称
         */
        private String name;

        /**
         * 加入日期
         */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date joinDate;

        /**
         * 退出日期
         */
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
        private Date leftDate;
    }
}
