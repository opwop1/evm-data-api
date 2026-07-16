package link.vtcm.forest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 * 限流代理配置
 * @author zhangmj
 * @since 2025/10/30
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "forest.rate-limit-proxy")
public class RateLimitProxyProperties {
    @NestedConfigurationProperty
    private PathProxyProperties pathProxy;
}
