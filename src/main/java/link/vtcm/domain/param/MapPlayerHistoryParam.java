package link.vtcm.domain.param;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author zhangmj
 * @since 2026/1/22
 */
@Data
public class MapPlayerHistoryParam {
    /**
     * TMP ID
     */
    private Integer tmpId;

    /**
     * 服务器ID
     */
    private Integer serverId;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
