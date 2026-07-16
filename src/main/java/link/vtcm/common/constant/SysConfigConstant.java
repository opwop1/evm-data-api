package link.vtcm.common.constant;

/**
 * 系统配置键常量
 * @author zhangmj
 * @since 2026/5/15
 */
public interface SysConfigConstant {
    String AI_TRANSLATION_MODEL = "ai.translation.model";
    String AI_TRANSLATION_ONLY_PROVIDER = "ai.translation.only_provider";
    String AI_TRANSLATION_SYSTEM_PROMPT = "ai.translation.system_prompt";
    String AI_TRANSLATION_SCENE_PROMPT_PREFIX = "ai.translation.scene.";
    String AI_TRANSLATION_SCENE_PROMPT_BAN_REASON = AI_TRANSLATION_SCENE_PROMPT_PREFIX + "ban_reason";
    String AI_TRANSLATION_SCENE_PROMPT_CHAT = AI_TRANSLATION_SCENE_PROMPT_PREFIX + "chat";
}
