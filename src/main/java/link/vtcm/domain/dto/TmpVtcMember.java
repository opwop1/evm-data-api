package link.vtcm.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * VTC成员
 * @author zhangmj
 * @since 2025/1/8
 */
@Data
public class TmpVtcMember {
    /**
     * VTC成员编号
     */
    @JsonProperty("id")
    private Integer memberId;

    /**
     * TMP编号
     */
    @JsonProperty("user_id")
    private Integer userId;

    /**
     * TMP名称
     */
    private String username;

    /**
     * TMP角色
     */
    private String role;

    /**
     * Steam 64位ID
     */
    @JsonProperty("steam_id")
    private String steamId;

    /**
     * 是否VTC所属人
     */
    @JsonProperty("is_owner")
    private Boolean isOwner;

    /**
     * 加入日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date joinDate;
}
