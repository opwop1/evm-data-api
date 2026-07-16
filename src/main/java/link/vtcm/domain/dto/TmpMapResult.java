package link.vtcm.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * TMP地图接口通用返回
 * @author zhangmj
 * @since 2025/4/11
 */
@Data
public class TmpMapResult<T> {
    @JsonProperty("Status")
    private Boolean status;
    @JsonProperty("Data")
    private T data;
}
