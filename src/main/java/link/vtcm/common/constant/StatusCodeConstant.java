package link.vtcm.common.constant;

/**
 * 状态码常量
 * @author zhangmj
 * @since 2025/8/21
 */
public interface StatusCodeConstant {
    /**
     * 成功
     */
    int SUCCESS = 200;

    /**
     * 失败
     */
    int FAIL = 500;

    /**
     * 玩家不存在
     */
    int PLAYER_NOT_EXIST = 10001;

    /**
     * VTC 不存在
     */
    int VTC_NOT_EXIST = 10002;

    /**
     * 翻译服务异常
     */
    int TRANS_PROVIDER_ERROR = 10003;
}
