package link.vtcm.domain.param;

import lombok.Data;

/**
 * 地图玩家列表查询入参封装
 * @author zhangmj
 * @since 2025/4/29
 */
@Data
public class MapPlayerListParam {
    /**
     * 服务器ID
     */
    private Integer serverId;

    /**
     * TMP名称
     */
    private String tmpName;

    /**
     * TMP ID
     */
    private Integer tmpId;

    /**
     * TMP ID 集合
     */
    private String tmpIdList;

    /**
     * A坐标X轴
     */
    private Integer aAxisX;

    /**
     * A坐标Y轴
     */
    private Integer aAxisY;

    /**
     * B坐标X轴
     */
    private Integer bAxisX;

    /**
     * B坐标Y轴
     */
    private Integer bAxisY;
}
