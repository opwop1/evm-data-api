package link.vtcm.common.util;

import link.vtcm.common.constant.StatusCodeConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 响应信息主体
 * @author Lion Li
 */
@Data
@NoArgsConstructor
public class R<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int code;

    private String msg;

    private T data;

    public static <T> R<T> ok() {
        return restResult(null, StatusCodeConstant.SUCCESS, "操作成功");
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, StatusCodeConstant.SUCCESS, "操作成功");
    }

    public static <T> R<T> ok(String msg) {
        return restResult(null, StatusCodeConstant.SUCCESS, msg);
    }

    public static <T> R<T> ok(String msg, T data) {
        return restResult(data, StatusCodeConstant.SUCCESS, msg);
    }

    public static <T> R<T> fail() {
        return restResult(null, StatusCodeConstant.FAIL, "操作失败");
    }

    public static <T> R<T> fail(String msg) {
        return restResult(null, StatusCodeConstant.FAIL, msg);
    }

    public static <T> R<T> fail(T data) {
        return restResult(data, StatusCodeConstant.FAIL, "操作失败");
    }

    public static <T> R<T> fail(String msg, T data) {
        return restResult(data, StatusCodeConstant.FAIL, msg);
    }

    public static <T> R<T> fail(int code, String msg) {
        return restResult(null, code, msg);
    }

    private static <T> R<T> restResult(T data, int code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setData(data);
        r.setMsg(msg);
        return r;
    }

    public static <T> Boolean isError(R<T> ret) {
        return !isSuccess(ret);
    }

    public static <T> Boolean isSuccess(R<T> ret) {
        return StatusCodeConstant.SUCCESS == ret.getCode();
    }
}
