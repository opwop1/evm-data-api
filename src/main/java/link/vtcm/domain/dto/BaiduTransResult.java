package link.vtcm.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 百度翻译返回
 * @author zhangmj
 * @since 2025/5/14
 */
@Data
public class BaiduTransResult {
    /**
     * 错误代码
     */
    @JsonProperty("error_code")
    private Integer errorCode;

    /**
     * 翻译结果
     */
    @JsonProperty("trans_result")
    private List<transResult> transResultList;

    @Data
    public static class transResult {
        /**
         * 原文
         */
        private String src;

        /**
         * 译文
         */
        private String dst;
    }
}
