package link.vtcm.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * TMP 文件信息
 * @author zhangmj
 * @since 2026/2/2
 */
@Data
public class TmpFileVO {
    /**
     * 类型
     */
    private Integer type;

    /**
     * 文件MD5
     */
    private String md5;

    /**
     * 文件大小，字节
     */
    private Long fileSize;

    /**
     * 文件路径
     */
    private String filePath;


    /**
     * 文件下载地址
     */
    private String downloadUrl;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
