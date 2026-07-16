package link.vtcm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import link.vtcm.domain.SysConfig;
import link.vtcm.mapper.SysConfigMapper;
import link.vtcm.service.SysConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysConfigServiceImpl implements SysConfigService {
    private final SysConfigMapper sysConfigMapper;
    private final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();
    private volatile long lastRefreshTime = 0;
    private static final long CACHE_TTL_MS = 60_000;

    @Override
    public String getValue(String key, Supplier<String> defaultValue) {
        refreshIfNeeded();
        String value = cache.get(key);
        if (value != null) {
            return value;
        }
        return defaultValue != null ? defaultValue.get() : null;
    }

    @Override
    public String getValue(String key) {
        return getValue(key, () -> null);
    }

    private void refreshIfNeeded() {
        long now = System.currentTimeMillis();
        if (now - lastRefreshTime < CACHE_TTL_MS) {
            return;
        }
        synchronized (this) {
            if (now - lastRefreshTime < CACHE_TTL_MS) {
                return;
            }
            try {
                sysConfigMapper.selectList(new LambdaQueryWrapper<SysConfig>()
                    .select(SysConfig::getConfigKey, SysConfig::getConfigValue))
                    .forEach(c -> cache.put(c.getConfigKey(), c.getConfigValue()));
                lastRefreshTime = now;
            } catch (Exception e) {
                log.warn("刷新系统配置缓存失败", e);
            }
        }
    }
}
