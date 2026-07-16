package link.vtcm.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhangmj
 * @since 2026/2/2
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun-oss")
public class AliyunOssProperties {
    private boolean enabled;
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
}
