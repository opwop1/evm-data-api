package link.vtcm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import link.vtcm.domain.OnlinePlayer;

import java.util.List;

/**
 * 在线玩家
 * @author zhangmj
 * @since 2025/4/10
 */
public interface OnlinePlayerMapper extends BaseMapper<OnlinePlayer> {
    /**
     * 批量插入
     */
    void insertBatch(List<OnlinePlayer> list);
}
