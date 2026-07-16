package link.vtcm.ai;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import link.vtcm.config.AiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对话模型工厂
 * @author zhangmj
 * @since 2026/5/15
 */
@Component
@RequiredArgsConstructor
public class ChatModelFactory {
    private final Map<String, ChatModel> chatModelMap = new ConcurrentHashMap<>();
    private final AiProperties aiProperties;
    private final Map<String, String> CUSTOM_HEADERS = MapUtil.<String, String>builder()
        .put("x-title", "EVM DATA API")
        .put("http-referer", "https://da.vtcm.link")
        .build();

    public ChatModel build(ChatModelConfig config) {
        return chatModelMap.computeIfAbsent(config.cacheKey(), k -> {
            OpenAiChatModel.OpenAiChatModelBuilder builder = OpenAiChatModel.builder()
                .baseUrl(aiProperties.getBaseUrl())
                .apiKey(aiProperties.getApiKey())
                .modelName(config.getModelName())
                .customHeaders(CUSTOM_HEADERS);
            if (config.getTemperature() != null) {
                builder.temperature(config.getTemperature());
            }
            if (config.getReasoningEffort() != null) {
                builder.reasoningEffort(config.getReasoningEffort());
            }
            Map<String, Object> customParam = MapUtil.newHashMap();
            if (CollUtil.isNotEmpty(config.getOnlyProviderList())) {
                customParam.put("provider", Map.of("only", config.getOnlyProviderList()));
            }
            builder.customParameters(customParam);
            return builder.build();
        });
    }

    public ChatModel build(String modelName) {
        return build(ChatModelConfig.builder().modelName(modelName).build());
    }
}
