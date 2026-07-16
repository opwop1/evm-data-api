package link.vtcm.forest.constant;

import link.vtcm.common.constant.CacheConstant;

/**
 * 限流代理相关常量
 * @author zhangmj
 * @since 2025/10/31
 */
public interface Constant {
    String LOCAL = "@local@";
    String RATE_LIMIT_PROXY = CacheConstant.RATE_LIMIT_PREFIX + "rate_limit_proxy:";
    String CACHE_PATH_PROXY = RATE_LIMIT_PROXY + "path_proxy:";
}
