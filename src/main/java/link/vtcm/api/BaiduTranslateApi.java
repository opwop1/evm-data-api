package link.vtcm.api;

import cn.hutool.crypto.SecureUtil;
import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Var;
import link.vtcm.domain.dto.BaiduTransResult;

/**
 * 百度翻译
 * @author zhangmj
 * @since 2025/5/14
 */
@BaseRequest(baseURL = "https://fanyi-api.baidu.com/api/trans/vip/translate")
public interface BaiduTranslateApi {

    /**
     * 翻译
     * @param appId 应用ID
     * @param sign 签名
     * @param salt 随机数
     * @param content 翻译内容
     */
    @Get("?from=auto&to=zh&appid={appId}&sign={sign}&salt={salt}&q={q}")
    BaiduTransResult trans(@Var("appId") String appId, @Var("sign") String sign, @Var("salt") Integer salt, @Var("q") String content);

    /**
     * 生成签名
     * @param appId 应用ID
     * @param key 密钥
     * @param salt 随机数
     * @param content 翻译内容
     */
    default String sign(String appId, String key, Integer salt, String content) {
        return SecureUtil.md5(appId + content + salt + key);
    }
}
