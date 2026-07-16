package link.vtcm.service;

import link.vtcm.domain.ApiLogs;

/**
 * API日志
 * @author zhangmj
 * @since 2025/7/18
 */
public interface ApiLogsService {
    /**
     * 保存
     */
    void save(ApiLogs apiLogs);
}