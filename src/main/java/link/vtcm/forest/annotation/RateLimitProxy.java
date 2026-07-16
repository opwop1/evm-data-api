package link.vtcm.forest.annotation;

import link.vtcm.forest.constant.ProxyTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 限流代理注解
 * @author zhangmj
 * @since 2025/10/30
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimitProxy {
    /**
     * 限流组
     */
    String value();

    /**
     * 代理类型
     */
    ProxyTypeEnum type();

    /**
     * 路径代理前缀（仅PATH_PROXY类型生效）
     */
    String pathPrefix() default "";

    /**
     * 使用本机
     */
    boolean useLocal() default true;

    /**
     * 限流时间周期，秒
     */
    int periodInSeconds() default 1;

    /**
     * 请求次数
     */
    int requests() default 1;
}
