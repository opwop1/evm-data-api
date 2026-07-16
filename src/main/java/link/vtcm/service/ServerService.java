package link.vtcm.service;

import link.vtcm.domain.Server;
import link.vtcm.domain.param.ServerListParam;
import link.vtcm.domain.vo.ServerVO;

import java.util.List;

/**
 * 服务器
 * @author zhangmj
 * @since 2025/7/1
 */
public interface ServerService {
    /**
     * 服务器列表
     */
    List<ServerVO> list(ServerListParam param);

    /**
     * 所有服务器列表
     */
    List<Server> selectAll();

    /**
     * 服务器信息
     */
    ServerVO info(Integer serverId);
}
