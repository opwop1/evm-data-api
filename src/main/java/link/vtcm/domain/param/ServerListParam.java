package link.vtcm.domain.param;

import lombok.Data;

/**
 * 服务器列表入参封装
 * @author zhangmj
 * @since 2025/4/28
 */
@Data
public class ServerListParam {
    /**
     * 是否在线 (1:是; 0:否; )
     */
    private Integer isOnline;
}
