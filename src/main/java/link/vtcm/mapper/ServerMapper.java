package link.vtcm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import link.vtcm.domain.Server;

import java.util.List;

/**
 * 服务器
 * @author zhangmj
 * @since 2025/4/28
 */
public interface ServerMapper extends BaseMapper<Server> {
    /**
     * 批量插入
     */
    void insertBatch(List<Server> list);
}
