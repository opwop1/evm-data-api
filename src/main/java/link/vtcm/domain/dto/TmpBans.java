package link.vtcm.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * TMP封禁信息
 * @author zhangmj
 * @since 2025/5/14
 */
@Data
public class TmpBans {
    /**
     * 封禁结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expiration;

    /**
     * 封禁时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timeAdded;

    /**
     * 原因
     */
    private String reason;
}
