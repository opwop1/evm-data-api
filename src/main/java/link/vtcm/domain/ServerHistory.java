package link.vtcm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 服务器人数历史
 * @author zhangmj
 * @since 2026/5/9
 */
@Data
@TableName("game_server_history")
public class ServerHistory {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 服务器 ID */
    private Integer serverId;

    /** 玩家数量 */
    private Integer playerCount;

    /** 更新时间 */
    private Date updateTime;
}
