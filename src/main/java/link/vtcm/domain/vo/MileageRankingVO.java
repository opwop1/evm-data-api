package link.vtcm.domain.vo;

import lombok.Data;

/**
 * 玩家里程排名
 * @author zhangmj
 * @since 2025/8/24
 */
@Data
public class MileageRankingVO {
    /**
     * TMP编号
     */
    private Integer tmpId;

    /**
     * 玩家名称
     */
    private String tmpName;

    /**
     * 里程 (米)
     */
    private Long distance;

    /**
     * 排名
     */
    private Integer ranking;
}
