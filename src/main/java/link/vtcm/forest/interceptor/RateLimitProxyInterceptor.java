package link.vtcm.forest.interceptor;

import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.interceptor.ForestInterceptor;
import link.vtcm.forest.annotation.RateLimitProxy;
import link.vtcm.forest.service.RateLimitProxyContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 限流代理拦截器
 * @author zhangmj
 * @since 2025/10/30
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimitProxyInterceptor implements ForestInterceptor {
    private final RateLimitProxyContext rateLimitProxyContext;

    @Override
    public boolean beforeExecute(ForestRequest req) {
        // 尝试获取注解
        Method method = req.getMethod().getMethod();
        RateLimitProxy annotation = method.getAnnotation(RateLimitProxy.class);
        if (annotation == null) {
            annotation = method.getDeclaringClass().getAnnotation(RateLimitProxy.class);
        }

        // 获取到注解，设置代理
        if (annotation != null) {
            rateLimitProxyContext.proxy(req, annotation);
        }

        return true;
    }
}
