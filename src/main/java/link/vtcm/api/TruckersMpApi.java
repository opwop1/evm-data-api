package link.vtcm.api;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Var;
import link.vtcm.domain.dto.TmpBans;
import link.vtcm.domain.dto.TmpPlayer;
import link.vtcm.domain.dto.TmpResult;
import link.vtcm.domain.dto.TmpServer;
import link.vtcm.domain.dto.TmpVersion;
import link.vtcm.domain.dto.TmpVtc;
import link.vtcm.domain.dto.TmpVtcMember;
import link.vtcm.domain.dto.TmpVtcMembers;
import link.vtcm.forest.annotation.RateLimitProxy;
import link.vtcm.forest.constant.ProxyTypeEnum;

import java.util.List;

/**
 * TMP接口
 * 速率限制：1分钟内最多60次，超过将触发限制1分钟
 * @author zhangmj
 * @since 2025/1/2
 */
@RateLimitProxy(value = "api.truckersmp.com", type = ProxyTypeEnum.PATH_PROXY, pathPrefix = "truckersmp", periodInSeconds = 60, requests = 60)
@BaseRequest(baseURL = "https://api.truckersmp.com")
public interface TruckersMpApi {
    /**
     * VTC所有成员
     * @param vtcId VTC ID
     */
    @Get("/v2/vtc/{vtcId}/members")
    TmpResult<TmpVtcMembers> vtcAllMember(@Var("vtcId") Integer vtcId);

    /**
     * 玩家信息
     * @param tmpId TMP编号
     */
    @Get("/v2/player/{tmpId}")
    TmpResult<TmpPlayer> player(@Var("tmpId") Integer tmpId);

    /**
     * VTC成员信息
     */
    @Get("/v2/vtc/{vtcId}/member/{memberId}")
    TmpResult<TmpVtcMember> vtcMember(@Var("vtcId") Integer vtcId, @Var("memberId") Integer memberId);

    /**
     * 服务器信息
     */
    @Get("/v2/servers")
    TmpResult<List<TmpServer>> servers();

    /**
     * 封禁信息
     */
    @Get("/v2/bans/{tmpId}")
    TmpResult<List<TmpBans>> bans(@Var("tmpId") Integer tmpId);

    /**
     * VTC信息
     */
    @Get("/v2/vtc/{vtcId}")
    TmpResult<TmpVtc> vtc(@Var("vtcId") Integer vtcId);

    /**
     * TMP 版本信息
     */
    @Get("/v2/version")
    TmpVersion version();
}
