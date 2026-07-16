package link.vtcm.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * TMP VTC信息
 * @author zhangmj
 * @since 2025/5/15
 */
@Data
public class TmpVtc {
    /**
     * VTC ID
     */
    private Integer id;

    /**
     * VTC名称
     */
    private String name;

    /**
     * 所有者TMP ID
     */
    @JsonProperty("owner_id")
    private Integer ownerId;

    /**
     * Logo地址
     */
    private String logo;

    /**
     * 封面地址
     */
    private String cover;

    /**
     * 成员数量
     */
    @JsonProperty("members_count")
    private Integer membersCount;

    /**
     * VTC验证，红名
     */
    private Boolean verified;

    /**
     * VTC验证，橙名
     */
    private Boolean validated;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created;
}
