package link.vtcm.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_config")
public class SysConfig {
    @TableId
    private Long id;
    /** 配置键 */
    private String configKey;
    /** 配置值 */
    private String configValue;
    /** 配置说明 */
    private String description;
    /** 更新时间 */
    private Date updateTime;
}
