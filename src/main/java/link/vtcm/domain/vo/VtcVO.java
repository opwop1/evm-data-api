package link.vtcm.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * VTC信息
 * @author zhangmj
 * @since 2025/5/15
 */
@Data
public class VtcVO {
    /**
     * VTC ID
     */
    private Integer vtcId;

    /**
     * VTC名称
     */
    private String name;

    /**
     * 认证等级
     */
    private Integer level;

    /**
     * 所有者TMP ID
     */
    private Integer ownerTmpId;

    /**
     * Logo地址
     */
    private String logoUrl;

    /**
     * 封面地址
     */
    private String coverUrl;

    /**
     * 成员数量
     */
    private Integer memberCount;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
