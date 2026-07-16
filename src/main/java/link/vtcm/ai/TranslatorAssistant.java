package link.vtcm.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * 翻译助手
 * @author zhangmj
 * @since 2026/5/15
 */
public interface TranslatorAssistant {
    @SystemMessage("{{systemPrompt}}")
    @UserMessage("{{text}}")
    String trans(@V("systemPrompt") String systemPrompt, @V("text") String text);
}
