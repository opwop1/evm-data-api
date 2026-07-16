package link.vtcm.common.constant;

/**
 * 缓存常量类
 * @author zhangmj
 * @since 2025/8/22
 */
public interface CacheConstant {
    /**
     * 翻译缓存
     */
    String KEY_TRANS = "trans:cache:";

    /**
     * 限流缓存前缀
     */
    String RATE_LIMIT_PREFIX = "rate_limit:";

    /**
     * VTC 成员明细缓存
     */
    String VTC_MEMBER_ALL = "vtc:member:all:";
}
