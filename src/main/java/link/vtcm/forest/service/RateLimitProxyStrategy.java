package link.vtcm.forest.service;

import com.dtflys.forest.http.ForestRequest;
import link.vtcm.forest.annotation.RateLimitProxy;
import link.vtcm.forest.constant.ProxyTypeEnum;

/**
 * 限流代理策略
 * @author zhangmj
 * @since 2025/10/30
 */
public interface RateLimitProxyStrategy {
    void proxy(ForestRequest<?> req, RateLimitProxy proxyAnnotation);
    ProxyTypeEnum type();
}
