package link.vtcm.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * DepotDownloader 工具配置
 * @author zhangmj
 * @since 2026/4/14
 */
@Data
@Component
@ConfigurationProperties(prefix = "depot-downloader")
public class DepotDownloaderProperties {
    private Boolean enabled = false;
    private String path;
    private String execute;
    private String exiftool;
    private Integer appId;
    private Integer depotId;
    private String steamUsername;
    private String steamPassword;
}
