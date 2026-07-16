package link.vtcm.controller;

import link.vtcm.common.util.R;
import link.vtcm.domain.param.ServerListParam;
import link.vtcm.domain.vo.ServerVO;
import link.vtcm.service.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 服务器
 * @author zhangmj
 * @since 2025/7/1
 */
@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
public class ServerController {
    private final ServerService serverService;

    /**
     * 服务器列表
     */
    @GetMapping("/list")
    public R<List<ServerVO>> list(ServerListParam param) {
        return R.ok(serverService.list(param));
    }

    /**
     * 服务器信息
     */
    @GetMapping("/info")
    public R<ServerVO> info(Integer serverId) {
        return R.ok(serverService.info(serverId));
    }
}
