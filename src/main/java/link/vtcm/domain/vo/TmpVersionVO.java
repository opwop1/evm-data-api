package link.vtcm.domain.vo;

import lombok.Data;

/**
 * TMP 版本信息
 * @author zhangmj
 * @since 2026/4/20
 */
@Data
public class TmpVersionVO {
    /**
     * TMP 插件版本
     */
    private String tmpVersion;

    /**
     * 兼容游戏版本
     */
    private String supportGameVersion;

    /**
     * 官方游戏版本
     */
    private String officialGameVersion;
}
