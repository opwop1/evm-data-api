package link.vtcm.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * VTC成员VO
 * @author zhangmj
 * @since 2025/7/4
 */
@Data
public class VtcMemberVO {
    /**
     * TMP编号
     */
    private Integer tmpId;

    /**
     * VTC成员编号
     */
    private Integer memberId;

    /**
     * TMP名称
     */
    private String name;

    /**
     * TMP角色
     */
    private String role;

    /**
     * 是否VTC所属人
     */
    private Boolean isOwner;

    /**
     * 加入日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date joinDate;
}
