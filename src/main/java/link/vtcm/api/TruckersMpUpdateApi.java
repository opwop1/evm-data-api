package link.vtcm.api;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Get;
import link.vtcm.domain.dto.TmpUpdateFileResult;

/**
 * TMP 更新服务接口
 * @author zhangmj
 * @since 2026/2/2
 */
@BaseRequest(baseURL = "https://update.ets2mp.com")
public interface TruckersMpUpdateApi {
    @Get("files.json")
    TmpUpdateFileResult files();
}
