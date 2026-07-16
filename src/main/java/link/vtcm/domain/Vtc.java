package link.vtcm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * VTC
 * @author zhangmj
 * @since 2025/5/15
 */
@Data
@TableName("game_vtc")
public class Vtc {
    /**
     * VTC ID
     */
    @TableId(type = IdType.INPUT)
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
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
