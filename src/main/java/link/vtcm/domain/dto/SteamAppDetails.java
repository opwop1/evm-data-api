package link.vtcm.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Steam游戏详情
 * @author zhangmj
 * @since 2025/6/30
 */
@Data
public class SteamAppDetails {
    public static final String TYPE_DLC = "dlc";

    @JsonProperty("steam_appid")
    private Integer steamAppId;

    private String type;

    private String name;

    @JsonProperty("is_free")
    private Boolean isFree;

    @JsonProperty("short_description")
    private String shortDescription;

    @JsonProperty("header_image")
    private String headerImage;

    private String background;

    @JsonProperty("price_overview")
    private PriceOverview priceOverview;

    private List<Integer> dlc;

    @Data
    public static class PriceOverview {
        @JsonProperty("initial")
        private Integer initialPrice;

        @JsonProperty("final")
        private Integer finalPrice;

        @JsonProperty("discount_percent")
        private Integer discountPercent;
    }
}
