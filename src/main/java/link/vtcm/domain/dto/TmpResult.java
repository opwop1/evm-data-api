package link.vtcm.domain.dto;

import lombok.Data;

/**
 * TMP接口通用返回
 * @author zhangmj
 * @since 2025/1/2
 */
@Data
public class TmpResult<T> {
    private Boolean error;
    private T response;
    private String descriptor;
}
