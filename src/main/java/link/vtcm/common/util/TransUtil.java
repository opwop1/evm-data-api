package link.vtcm.common.util;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.SecureUtil;
import link.vtcm.api.BaiduTranslateApi;
import link.vtcm.common.constant.CacheConstant;
import link.vtcm.config.BaiduTranslateProperties;
import link.vtcm.domain.dto.BaiduTransResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 翻译工具类
 * @author zhangmj
 * @since 2025/9/18
 */
@Component
@RequiredArgsConstructor
public class TransUtil {
    private final BaiduTranslateApi baiduTranslateApi;
    private final BaiduTranslateProperties baiduTranslateProperties;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 翻译
     * @param content 内容
     * @param cache 是否缓存
     */
    public String trans(String content, boolean cache) {
        // 尝试从缓存中获取翻译结果
        String cacheKey = CacheConstant.KEY_TRANS + SecureUtil.md5(content);
        if (cache && redisTemplate.hasKey(cacheKey)) {
            return redisTemplate.opsForValue().get(cacheKey);
        }

        // 调用翻译服务
        String appId = baiduTranslateProperties.getAppId();
        String key = baiduTranslateProperties.getKey();
        int randomInt = RandomUtil.randomInt(100000, 999999);
        BaiduTransResult transResult = baiduTranslateApi.trans(appId, baiduTranslateApi.sign(appId, key, randomInt, content), randomInt, URLUtil.encodeAll(content));
        if (transResult.getErrorCode() != null) {
            return null;
        }

        // 开启了缓存，将翻译结果写入缓存
        String trans = transResult.getTransResultList().get(0).getDst();
        if (cache) {
            redisTemplate.opsForValue().set(cacheKey, trans, 30, TimeUnit.DAYS);
        }

        return trans;
    }
}
