package link.vtcm.domain.dto;

import lombok.Builder;
import lombok.Data;

/**
 * TMP地图区域查询接口入参封装
 * @author zhangmj
 * @since 2025/4/10
 */
@Data
@Builder
public class TmpMapAreaParam {
    private Integer x1;
    private Integer y1;
    private Integer x2;
    private Integer y2;
    private Integer server;
}
