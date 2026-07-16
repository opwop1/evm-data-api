package link.vtcm.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * TMP 最新文件
 * @author zhangmj
 * @since 2026/2/2
 */
@Data
public class TmpUpdateFileResult {
    @JsonProperty("Files")
    private List<TmpUpdateFile> files;
}
