package link.vtcm.common.constant;

/**
 * 同步锁常量
 * @author zhangmj
 * @since 2025/7/1
 */
public interface LockConstant {
    /**
     * 全局RedisKey前缀
     */
    String GLOBAL_REDIS_KEY = "lock:";

    /**
     * 服务器数据同步操作
     */
    String SYNC_SERVER = GLOBAL_REDIS_KEY + "sync:server";

    /**
     * 在线玩家数据同步操作
     */
    String SYNC_ONLINE_PLAYER = GLOBAL_REDIS_KEY + "sync:online_player";

    /**
     * 玩家数据同步操作:tmpId
     */
    String SYNC_PLAYER = GLOBAL_REDIS_KEY + "sync:player:";

    /**
     * VTC数据同步操作:vtcId
     */
    String SYNC_VTC = GLOBAL_REDIS_KEY + "sync:vtc:";

    /**
     * 翻译锁
     */
    String TRANS_LOCK = GLOBAL_REDIS_KEY + "trans:lock:";
}
