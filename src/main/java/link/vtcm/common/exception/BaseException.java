package link.vtcm.common.exception;

import link.vtcm.common.constant.StatusCodeConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 基础异常
 * @author zhangmj
 * @since 2025/7/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {
    /**
     * 错误码
     */
    private Integer code;
    /**
     * 错误消息
     */
    private String message;

    public BaseException(String message) {
        super(message);
        this.code = StatusCodeConstant.FAIL;
        this.message = message;
    }

    public BaseException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
