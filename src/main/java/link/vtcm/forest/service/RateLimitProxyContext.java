package link.vtcm.forest.service;

import cn.hutool.core.map.MapUtil;
import com.dtflys.forest.http.ForestRequest;
import link.vtcm.forest.annotation.RateLimitProxy;
import link.vtcm.forest.constant.ProxyTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author zhangmj
 * @since 2025/10/31
 */
@Slf4j
@Component
public class RateLimitProxyContext {
    private final Map<ProxyTypeEnum, RateLimitProxyStrategy> strategyMap = MapUtil.newHashMap();

    public RateLimitProxyContext(List<RateLimitProxyStrategy> strategyList) {
        for (RateLimitProxyStrategy strategy : strategyList) {
            strategyMap.put(strategy.type(), strategy);
        }
    }

    public void proxy(ForestRequest<?> req, RateLimitProxy annotation) {
        log.info("proxy::开始设置请求代理 type={}, url={}", annotation.type().toString(), req.getUrl());
        RateLimitProxyStrategy strategy = strategyMap.get(annotation.type());
        if (strategy != null) {
            strategy.proxy(req, annotation);
        }
    }
}
