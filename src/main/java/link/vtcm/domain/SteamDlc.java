package link.vtcm.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import link.vtcm.common.constant.GameConstant;
import lombok.Data;

import java.util.Date;

/**
 * Steam DLC
 * @author zhangmj
 * @since 2025/6/30
 */
@Data
@TableName("game_steam_dlc")
public class SteamDlc {
    @TableId
    private Long id;

    /**
     * Steam App Id
     */
    private Integer appId;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     * @see GameConstant 类型枚举
     */
    private Integer type;

    /**
     * 说明
     */
    private String desc;

    /**
     * 标题图片地址
     */
    private String headerImageUrl;

    /**
     * 背景图片地址
     */
    private String backgroundImageUrl;

    /**
     * 原价 (分)
     */
    private Integer originalPrice;

    /**
     * 现价 (分)
     */
    private Integer finalPrice;

    /**
     * 折扣百分比
     */
    private Integer discount;

    /**
     * 更新时间
     */
    private Date updateTime;
}
