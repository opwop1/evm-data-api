package link.vtcm.service.impl;

import link.vtcm.domain.ApiLogs;
import link.vtcm.mapper.ApiLogsMapper;
import link.vtcm.service.ApiLogsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * API日志服务实现
 * @author zhangmj
 * @since 2025/7/18
 */
@Service
@RequiredArgsConstructor
public class ApiLogsServiceImpl implements ApiLogsService {
    private final ApiLogsMapper baseMapper;

    @Override
    public void save(ApiLogs apiLogs) {
        baseMapper.insert(apiLogs);
    }
}