package link.vtcm.schedule;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import link.vtcm.api.TruckersMpApi;
import link.vtcm.common.exception.BaseException;
import link.vtcm.config.DepotDownloaderProperties;
import link.vtcm.domain.GameTmpVersion;
import link.vtcm.domain.dto.TmpVersion;
import link.vtcm.service.GameTmpVersionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zeroturnaround.exec.ProcessExecutor;

import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * 游戏版本同步定时任务
 * @author zhangmj
 * @since 2026/4/20
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TmpVersionSyncJob {
    private final DepotDownloaderProperties depotDownloaderProperties;
    private final TruckersMpApi truckersMpApi;
    private final GameTmpVersionService gameTmpVersionService;

    @Scheduled(cron = "0 */10 * * * *")
    public void job() {
        TimeInterval timer = DateUtil.timer();
        log.info("job::TMP 版本信息更新开始处理");

        try {
            // 实际游戏版本号需通过 depot-downloader 从 Steam 下载游戏主程序后读取；
            // 未启用时跳过该步骤，仅更新 TMP 版本信息（officialGameVersion 保留原值）
            String version = null;
            if (Boolean.TRUE.equals(depotDownloaderProperties.getEnabled())) {
                // 下载游戏程序文件
                String execute = Paths.get(depotDownloaderProperties.getPath(), depotDownloaderProperties.getExecute()).toString();
                String fileList = Paths.get(depotDownloaderProperties.getPath(), "file.txt").toString();
                log.info("job::正在下载游戏主程序文件");
                new ProcessExecutor()
                    .directory(FileUtil.file(depotDownloaderProperties.getPath()))
                    .timeout(60, TimeUnit.SECONDS)
                    .command(execute,
                            "-app", depotDownloaderProperties.getAppId().toString(),
                            "-depot", depotDownloaderProperties.getDepotId().toString(),
                            "-filelist", fileList,
                            "-remember-password",
                            "-username", depotDownloaderProperties.getSteamUsername(),
                            "-password", depotDownloaderProperties.getSteamPassword())
                    .execute();
                log.info("job::下载游戏主程序文件完成");

                // 调用 exif-tool 获取版本号
                String exePath = Paths.get(depotDownloaderProperties.getPath(), "depots", depotDownloaderProperties.getDepotId().toString()).toString();
                exePath = Paths.get(FileUtil.ls(exePath)[0].getPath(), "bin", "win_x64", "eurotrucks2.exe").toString();
                version = new ProcessExecutor()
                    .command(depotDownloaderProperties.getExiftool(),
                            "-ProductVersion",
                            exePath,
                            "-S3")
                    .readOutput(true)
                    .execute()
                    .outputUTF8();
                version = StrUtil.trim(version);
            } else {
                log.info("job::depot-downloader 未启用，跳过实际游戏版本获取");
            }

            // 调用 tmp 版本信息接口
            TmpVersion tmpVersion = truckersMpApi.version();
            if (tmpVersion == null) {
                throw new BaseException("获取 TMP 版本信息失败");
            }

            // 构建数据并更新
            GameTmpVersion entity = new GameTmpVersion();
            entity.setTmpVersion(tmpVersion.getName());
            entity.setSupportGameVersion(tmpVersion.getSupportedGameVersion());
            if (version != null) {
                entity.setOfficialGameVersion(version + "s");
            }
            gameTmpVersionService.set(entity);
        } catch (Exception e) {
            log.error("job::TMP 版本更新处理失败", e);
        } finally {
            if (Boolean.TRUE.equals(depotDownloaderProperties.getEnabled())) {
                FileUtil.del(Paths.get(depotDownloaderProperties.getPath(), "depots"));
            }
        }
        log.info("job::TMP 版本信息更新处理完成 time={}", timer.intervalMs());
    }
}
