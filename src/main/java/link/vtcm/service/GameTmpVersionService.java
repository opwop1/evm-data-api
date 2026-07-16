package link.vtcm.service;

import link.vtcm.domain.GameTmpVersion;

/**
 * TMP 版本信息
 * @author zhangmj
 * @since 2026/4/20
 */
public interface GameTmpVersionService {
    void set(GameTmpVersion entity);
    GameTmpVersion get();
}
