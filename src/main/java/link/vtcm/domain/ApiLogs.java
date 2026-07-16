package link.vtcm.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * API日志
 * @author zhangmj
 * @since 2025/7/18
 */
@Data
@TableName("api_logs")
public class ApiLogs {
    @TableId
    private Long id;

    /**
     * 请求时间
     */
    private Date requestTime;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 完整请求路径
     */
    private String fullUrl;

    /**
     * 请求路径
     */
    private String url;

    /**
     * 客户端IP
     */
    private String clientIp;

    /**
     * 请求参数
     */
    private String param;

    /**
     * 响应耗时 (毫秒)
     */
    private Integer responseTime;
}
