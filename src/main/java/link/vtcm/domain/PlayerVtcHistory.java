package link.vtcm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 玩家VTC历史信息
 * @author zhangmj
 * @since 2025/5/21
 */
@Data
@TableName("game_player_vtc_history")
public class PlayerVtcHistory {
    /**
     * TMP编号
     */
    private Integer tmpId;

    /**
     * VTC编号
     */
    private Integer vtcId;

    /**
     * VTC名称
     */
    private String vtcName;

    /**
     * 加入日期
     */
    private Date joinDate;

    /**
     * 退出日期
     */
    private Date quitDate;
}
