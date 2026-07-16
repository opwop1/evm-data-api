package link.vtcm.domain.param;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 玩家里程排行榜入参
 * @author zhangmj
 * @since 2025/4/29
 */
@Data
public class MileageRankingParam {
    public static final Integer RANKING_TYPE_TOTAL = 1;
    public static final Integer RANKING_TYPE_TODAY = 2;

    /**
     * 排行榜类型 (1:总里程; 2:当日里程; )
     */
    @NotNull(message = "排行榜类型不能为空")
    @Min(value = 1, message = "排行榜类型无效")
    @Max(value = 2, message = "排行榜类型无效")
    private Integer rankingType;

    /**
     * TMP编号
     */
    @Min(value = 1, message = "TMP编号必须大于0")
    private Integer tmpId;

    /**
     * 排行榜数量
     */
    @Min(value = 1, message = "排行榜数量必须大于0")
    @Max(value = 100, message = "排行榜数量不能超过100")
    private Integer rankingCount;
}
