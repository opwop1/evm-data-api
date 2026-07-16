package link.vtcm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * TMP 文件信息
 * @author zhangmj
 * @since 2026/2/2
 */
@Data
@TableName("game_tmp_file")
public class TmpFile {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 文件MD5
     */
    private String md5;

    /**
     * 文件大小, 字节
     */
    private Long fileSize;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * OSS Object Key
     */
    private String ossObjectKey;

    /**
     * OSS 文件下载地址
     */
    private String ossUrl;

    /**
     * 更新时间
     */
    private Date updateTime;
}
