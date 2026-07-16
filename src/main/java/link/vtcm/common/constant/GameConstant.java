package link.vtcm.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 游戏相关常量
 * @author zhangmj
 * @since 2025/4/30
 */
public interface GameConstant {
    /**
     * Steam DLC 类型枚举
     */
    @Getter
    @AllArgsConstructor
    enum GameSteamDlcTypeEnum {
        MAP(1, "地图"),
        CARGO(2, "货物"),
        TRAILER(3, "挂车"),
        TUNING_CABIN(4, "改装/内饰"),
        PAINT_THEMES(5, "涂装"),
        FREE(6, "免费"),
        ;

        private final Integer type;
        private final String name;
    }

    /**
     * VTC认证等级
     */
    @Getter
    @AllArgsConstructor
    enum VtcLevelEnum {
        NORMAL(1, "普通"),
        ORANGE(2, "橙名"),
        RED(3, "红名"),
        ;

        private final Integer level;
        private final String name;
    }

    /**
     * 地图标点类型枚举
     */
    @Getter
    @AllArgsConstructor
    enum MapMarkerTypeEnum {
        COUNTRY(1, "国家"),
        CITY(2, "城市"),
        COMPANY(3, "货场"),
        TRUCK_DEALER(4, "卡车销售商"),
        PARKING(5, "停车场"),
        SERVICE(6, "修理厂"),
        RECRUITMENT(7, "司机介绍所"),
        FERRY(8, "渡轮"),
        GARAGE(9, "车库"),
        FUEL(10, "加油站"),
        TRAIN(11, "火车站"),
        VIEWPOINT(12, "观景点"),
        WEIGHT_STATION(13, "称重站"),
        BUS_STOP(14, "巴士站"),
        TOLL_STATION(15, "收费站"),
        OTHER(99, "其他"),
        ;

        private final Integer code;
        private final String name;
    }

    /**
     * 地图类型枚举
     */
    @Getter
    @AllArgsConstructor
    enum MapMarkerMapTypeEnum {
        ETS(1, "ets"),
        PROMODS(2, "promods"),
        ;

        private final Integer code;
        private final String name;
    }

    /**
     * TMP 文件类型枚举
     */
    @Getter
    @AllArgsConstructor
    enum TmpFileTypeEnum {
        UNKNOWN(0, "unknown", "未知"),
        SYSTEM(1, "system", "系统"),
        ETS(2, "ets2", "欧卡"),
        ATS(3, "ats", "美卡"),
        ;

        private final Integer code;
        private final String type;
        private final String name;

        public static TmpFileTypeEnum fromType(String type) {
            for (TmpFileTypeEnum value : values()) {
                if (value.type.equals(type)) {
                    return value;
                }
            }
            return null;
        }
    }
}
