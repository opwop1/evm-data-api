package link.vtcm.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 常量类
 * @author zhangmj
 * @since 2025/7/1
 */
public interface Constant {
    Integer ENABLE = 1;
    Integer DISABLE = 0;

    /**
     * 翻译场景类型枚举
     */
    @Getter
    @AllArgsConstructor
    enum TransSceneTypeEnum {
        BAN_REASON(1, "封禁原因", SysConfigConstant.AI_TRANSLATION_SCENE_PROMPT_BAN_REASON),
        CHAT(2, "聊天", SysConfigConstant.AI_TRANSLATION_SCENE_PROMPT_CHAT),
        ;

        private final Integer code;
        private final String name;
        private final String promptKey;

        public static TransSceneTypeEnum fromCode(Integer code) {
            for (TransSceneTypeEnum e : values()) {
                if (e.code.equals(code)) {
                    return e;
                }
            }
            return null;
        }
    }
}
