package link.vtcm.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * @author zhangmj
 * @since 2026/5/15
 */
public interface Assistant {
    String chat(String msg);

    @SystemMessage("{{system}}")
    @UserMessage("{{user}}")
    String chat(@V("system") String system, @V("user") String user);
}
