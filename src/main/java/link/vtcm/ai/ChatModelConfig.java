package link.vtcm.ai;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author zhangmj
 * @since 2026/5/19
 */
@Data
@Builder
public class ChatModelConfig {
    private String modelName;
    private List<String> onlyProviderList;
    private Double temperature;
    private String reasoningEffort;

    String cacheKey() {
        return modelName
            + (CollUtil.isNotEmpty(onlyProviderList) ? ":p=" + CollUtil.join(onlyProviderList, ",") : StrUtil.EMPTY)
            + (temperature != null ? ":t=" + temperature : StrUtil.EMPTY)
            + (reasoningEffort != null ? ":r=" + reasoningEffort : StrUtil.EMPTY);
    }
}
