package link.vtcm.common.aspect;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.map.MapUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletRequest;
import link.vtcm.domain.ApiLogs;
import link.vtcm.service.ApiLogsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

/**
 * API日志切面
 * @author zhangmj
 * @since 2025/7/18
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ApiLoggerAspect {
    private final ApiLogsService apiLogsService;
    @Around("execution(* link.vtcm.controller..*.*(..))")
    public Object logApiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return joinPoint.proceed();
        }

        HttpServletRequest request = attributes.getRequest();
        TimeInterval timer = DateUtil.timer();
        
        // 记录请求日志
        String clientIP = JakartaServletUtil.getClientIP(request);
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String fullUrl = request.getRequestURL().toString();
        Map<String, String[]> parameterMap = request.getParameterMap();
        String paramJson = MapUtil.isEmpty(parameterMap) ? null : JSON.toJSONString(parameterMap);
        
        if (MapUtil.isEmpty(parameterMap)) {
            log.info("[API] {} => [{}]{}", clientIP, method, uri);
        } else {
            log.info("[API] {} => [{}]{} param={}", clientIP, method, uri, paramJson);
        }
        
        // 创建API日志对象
        ApiLogs apiLogs = new ApiLogs();
        apiLogs.setRequestTime(DateUtil.date());
        apiLogs.setRequestMethod(method);
        apiLogs.setFullUrl(fullUrl);
        apiLogs.setUrl(uri);
        apiLogs.setClientIp(clientIP);
        apiLogs.setParam(paramJson);
        
        try {
            // 执行目标方法
            Object result = joinPoint.proceed();
            
            // 记录响应时间
            int responseTime = (int) timer.intervalMs();
            apiLogs.setResponseTime(responseTime);
            
            // 记录响应日志
            log.info("[API] {} <= [{}]{} time={}ms", clientIP, method, uri, responseTime);
            
            return result;
        } catch (Exception e) {
            // 记录异常日志
            int responseTime = (int) timer.intervalMs();
            apiLogs.setResponseTime(responseTime);
            log.error("[API] {} <= [{}]{} time={}ms error={}", clientIP, method, uri, responseTime, e.getMessage());
            throw e;
        } finally {
            apiLogsService.save(apiLogs);
        }
    }
}