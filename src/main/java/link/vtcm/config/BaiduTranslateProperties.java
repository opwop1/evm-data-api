package link.vtcm.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 百度翻译配置
 * @author zhangmj
 * @since 2026/7/16
 */
@Data
@Component
@ConfigurationProperties(prefix = "baidu-translate")
public class BaiduTranslateProperties {
    private String appId;
    private String key;
}
