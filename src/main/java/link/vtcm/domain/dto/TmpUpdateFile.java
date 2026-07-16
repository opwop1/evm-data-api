package link.vtcm.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * TMP 文件信息
 * @author zhangmj
 * @since 2026/2/2
 */
@Data
public class TmpUpdateFile {
    @JsonProperty("Md5")
    private String md5;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("FilePath")
    private String filePath;
}
