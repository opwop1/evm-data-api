package link.vtcm.api;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Get;
import link.vtcm.forest.annotation.RateLimitProxy;
import link.vtcm.forest.constant.ProxyTypeEnum;
import link.vtcm.domain.dto.TmpMapPlayer;
import link.vtcm.domain.dto.TmpMapResult;

import java.util.List;

/**
 * TMP地图接口
 * @author zhangmj
 * @since 2025/4/10
 */
@RateLimitProxy(value = "tracker.ets2map.com", type = ProxyTypeEnum.PATH_PROXY, pathPrefix = "ets2map", periodInSeconds = 60)
@BaseRequest(baseURL = "https://tracker.ets2map.com/v3")
public interface TruckersMapApi {
    /**
     * 所有玩家
     */
    @Get("/fullmap")
    TmpMapResult<List<TmpMapPlayer>> fullMap();
}
