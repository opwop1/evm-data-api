package link.vtcm.forest.config;

import lombok.Data;

import java.util.List;

/**
 * 路径代理配置
 * @author zhangmj
 * @since 2025/10/30
 */
@Data
public class PathProxyProperties {
    private Boolean enabled = false;
    private List<String> urls;
}
