package link.vtcm.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Aliyun OSS 配置类
 * @author zhangmj
 * @since 2026/2/2
 */
@Configuration
public class AliyunOssConfig {
    @Bean(destroyMethod = "shutdown")
    @ConditionalOnProperty(prefix = "aliyun-oss", name = "enabled", havingValue = "true")
    public OSS ossClient(AliyunOssProperties properties) {
        return new OSSClientBuilder().build(
                properties.getEndpoint(),
                properties.getAccessKey(),
                properties.getSecretKey()
        );
    }
}
