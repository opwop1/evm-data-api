package link.vtcm.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * TMP 版本信息
 * @author zhangmj
 * @since 2026/2/12
 */
@Data
public class TmpVersion {
    private String name;
    @JsonProperty("supported_game_version")
    private String supportedGameVersion;
}
