package link.vtcm.schedule;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import link.vtcm.api.SteamAppDetailsApi;
import link.vtcm.domain.SteamDlc;
import link.vtcm.domain.dto.SteamAppDetails;
import link.vtcm.domain.dto.SteamAppDetailsResult;
import link.vtcm.mapper.SteamDlcMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * SteamDLC同步定时任务
 * @author zhangmj
 * @since 2025/7/1
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SteamDlcSyncJob {
    private final SteamDlcMapper steamDlcMapper;
    private final SteamAppDetailsApi steamAppDetailsApi;

    @Scheduled(cron = "0 0 0/1 * * *")
    public void job() {
        final int ETS_APP_ID = 227300;
        log.info("job::开始同步Steam DLC信息");

        // 查询欧卡游戏信息，从信息中获取DLC ID列表
        Map<Integer, SteamAppDetailsResult<SteamAppDetails>> etsAppResult = steamAppDetailsApi.appDetails(ETS_APP_ID);
        if (etsAppResult == null || !etsAppResult.get(ETS_APP_ID).getSuccess()) {
            log.warn("syncJob::获取欧卡游戏信息失败");
            return;
        }
        List<Integer> etsDlcAppIdList = etsAppResult.get(ETS_APP_ID).getData().getDlc();

        // 遍历更新 DLC 信息
        List<SteamDlc> steamDlcList = CollUtil.newArrayList();
        for (Integer appId : etsDlcAppIdList) {
            log.info("syncJob::正在更新 DLC appId={}", appId);
            // 获取 DLC 信息
            Map<Integer, SteamAppDetailsResult<SteamAppDetails>> dlcAppResult;
            try {
                dlcAppResult = steamAppDetailsApi.appDetails(appId);
            } catch (Exception e) {
                log.warn("syncJob::获取 DLC 信息异常 appId={}", appId, e);
                ThreadUtil.sleep(2000);
                continue;
            }
            if (dlcAppResult == null || !dlcAppResult.get(appId).getSuccess()) {
                log.warn("syncJob::获取 DLC 信息失败");
                continue;
            }
            SteamAppDetails dlcApp = dlcAppResult.get(appId).getData();

            // 仅处理 DLC 类型的数据
            if (!SteamAppDetails.TYPE_DLC.equals(dlcApp.getType())) {
                continue;
            }

            // 构建 DLC 信息
            steamDlcList.add(this.buildEntity(dlcApp));

            // 睡眠2秒，避免请求过快
            ThreadUtil.sleep(2000);
        }

        // 保存数据
        this.saveOrUpdateDlc(steamDlcList);
    }

    private SteamDlc buildEntity(SteamAppDetails dlcApp) {
        SteamDlc steamDlc = new SteamDlc();
        steamDlc.setAppId(dlcApp.getSteamAppId());
        steamDlc.setName(StrUtil.removePrefix(dlcApp.getName(), "Euro Truck Simulator 2 - "));
        steamDlc.setDesc(dlcApp.getShortDescription());
        steamDlc.setHeaderImageUrl(dlcApp.getHeaderImage());
        steamDlc.setBackgroundImageUrl(dlcApp.getBackground());
        steamDlc.setUpdateTime(DateUtil.date());

        // 价格信息
        if (dlcApp.getIsFree() || dlcApp.getPriceOverview() == null) {
            steamDlc.setOriginalPrice(0);
            steamDlc.setFinalPrice(0);
            steamDlc.setDiscount(0);
        } else {
            SteamAppDetails.PriceOverview price = dlcApp.getPriceOverview();
            steamDlc.setFinalPrice(price.getFinalPrice());
            steamDlc.setOriginalPrice(price.getInitialPrice());
            steamDlc.setDiscount(price.getDiscountPercent());
        }

        // 尝试获取现有的DLC数据
        SteamDlc existsSteamDlc = steamDlcMapper.selectOne(new LambdaQueryWrapper<SteamDlc>().eq(SteamDlc::getAppId, steamDlc.getAppId()));
        if (existsSteamDlc != null) {
            steamDlc.setId(existsSteamDlc.getId());
        }

        return steamDlc;
    }

    private void saveOrUpdateDlc(List<SteamDlc> steamDlcList) {
        for (SteamDlc steamDlc : steamDlcList) {
            // 存在更新，否则新增
            if (steamDlc.getId() != null) {
                steamDlcMapper.updateById(steamDlc);
            } else {
                steamDlcMapper.insert(steamDlc);
            }
        }
    }
}
