package link.vtcm.forest.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.dtflys.forest.http.ForestRequest;
import link.vtcm.common.exception.BaseException;
import link.vtcm.forest.annotation.RateLimitProxy;
import link.vtcm.forest.config.RateLimitProxyProperties;
import link.vtcm.forest.constant.Constant;
import link.vtcm.forest.constant.ProxyTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.Duration;

/**
 * 路径代理实现
 * 将原始URL的path+query拼接到代理baseUrl后
 * @author zhangmj
 * @since 2025/10/30
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PathProxy implements RateLimitProxyStrategy {
    private final RateLimitProxyProperties properties;
    private final RedissonClient redissonClient;

    @Override
    public ProxyTypeEnum type() {
        return ProxyTypeEnum.PATH_PROXY;
    }

    @Override
    public void proxy(ForestRequest<?> req, RateLimitProxy annotation) {
        if (!properties.getPathProxy().getEnabled() || CollUtil.isEmpty(properties.getPathProxy().getUrls())) {
            return;
        }

        String urlPrefix = this.obtainUrl(annotation);
        if (StrUtil.isEmpty(urlPrefix)) {
            throw new BaseException("未获取到可用代理前缀");
        } else if (Constant.LOCAL.equals(urlPrefix)) {
            log.info("proxy::使用本机直接访问");
            return;
        }

        try {
            URI uri = new URI(req.getUrl());
            String pathAndQuery = uri.getPath();
            if (uri.getQuery() != null) {
                pathAndQuery += "?" + uri.getQuery();
            }
            String pathPrefix = annotation.pathPrefix();
            if (StrUtil.isNotBlank(pathPrefix) && !pathPrefix.startsWith("/")) {
                pathPrefix = "/" + pathPrefix;
            }
            String url = urlPrefix + pathPrefix + pathAndQuery;
            log.info("proxy::设置代理 url={}", url);
            req.setUrl(url);
        } catch (Exception e) {
            log.error("proxy::解析URL失败 url={}", req.getUrl(), e);
        }
    }

    private String obtainUrl(RateLimitProxy annotation) {
        if (annotation.useLocal() && this.available(annotation, Constant.LOCAL)) {
            return Constant.LOCAL;
        }
        for (String url : properties.getPathProxy().getUrls()) {
            if (this.available(annotation, url)) {
                return url;
            }
        }
        return null;
    }

    private boolean available(RateLimitProxy annotation, String url) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(Constant.CACHE_PATH_PROXY + annotation.value() + StringPool.COLON + SecureUtil.md5(url));
        rateLimiter.trySetRate(RateType.OVERALL, annotation.requests(), Duration.ofSeconds(annotation.periodInSeconds()), Duration.ofHours(2));
        return rateLimiter.tryAcquire();
    }
}
