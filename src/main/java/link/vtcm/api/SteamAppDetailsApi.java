package link.vtcm.api;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Headers;
import com.dtflys.forest.annotation.Var;
import link.vtcm.domain.dto.SteamAppDetails;
import link.vtcm.domain.dto.SteamAppDetailsResult;

import java.util.Map;

/**
 * Steam应用详情
 * @author zhangmj
 * @since 2025/6/30
 */
@BaseRequest(baseURL = "https://store.steampowered.com/api/appdetails")
public interface SteamAppDetailsApi {
    @Headers("Accept-Language: zh-CN,zh;q=0.9")
    @Get("?cc=CN&appids={appId}")
    Map<Integer, SteamAppDetailsResult<SteamAppDetails>> appDetails(@Var("appId") Integer appId);
}
