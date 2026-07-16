package link.vtcm.schedule;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.aliyun.oss.OSS;
import link.vtcm.api.TruckersMpUpdateApi;
import link.vtcm.common.constant.GameConstant;
import link.vtcm.common.exception.BaseException;
import link.vtcm.config.AliyunOssProperties;
import link.vtcm.domain.TmpFile;
import link.vtcm.domain.dto.TmpUpdateFile;
import link.vtcm.domain.dto.TmpUpdateFileResult;
import link.vtcm.mapper.TmpFileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * TMP 文件更新任务
 * @author zhangmj
 * @since 2026/2/2
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "aliyun-oss", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class TmpFileSyncJob {
    private final TmpFileMapper tmpFileMapper;
    private final OSS ossClient;
    private final AliyunOssProperties aliyunOssProperties;
    private final TruckersMpUpdateApi truckersMpUpdateApi;

    @Scheduled(cron = "0 */10 * * * *")
    public void job() {
        TimeInterval timer = DateUtil.timer();
        log.info("job::开始更新 TMP 文件");

        // 获取最新文件
        TmpUpdateFileResult tmpUpdateFileResult = truckersMpUpdateApi.files();
        if (tmpUpdateFileResult == null || CollUtil.isEmpty(tmpUpdateFileResult.getFiles())) {
            throw new BaseException("获取最新文件信息失败");
        }

        // 查询现有文件
        List<TmpFile> tmpFileList = tmpFileMapper.selectList(null);
        Map<String, TmpFile> tmpFileMap = tmpFileList.stream().collect(Collectors.toMap(TmpFile::getFilePath, tmpFile -> tmpFile));

        // 遍历文件检查变更
        Date now = DateUtil.date();
        for (TmpUpdateFile updateFile : tmpUpdateFileResult.getFiles()) {
            TmpFile tmpFile = tmpFileMap.get(updateFile.getFilePath());

            String tmpPath = Paths.get(FileUtil.getTmpDirPath(), UUID.randomUUID().toString(true)).toString();
            try {
                File downloadTmpFile = FileUtil.file(Paths.get(tmpPath, updateFile.getFilePath()).toString());
                String objectKey = "tmp_file" + updateFile.getFilePath();

                // 保存新文件
                if (tmpFile == null) {
                    log.info("job::保存新文件 file={}", updateFile.getFilePath());

                    // 上传文件到 OSS
                    long byteCount = this.reuploadFile(updateFile, objectKey, downloadTmpFile);

                    // 保存文件信息
                    GameConstant.TmpFileTypeEnum tmpFileTypeEnum = GameConstant.TmpFileTypeEnum.fromType(updateFile.getType());
                    TmpFile newTmpFile = new TmpFile();
                    newTmpFile.setType(tmpFileTypeEnum != null ? tmpFileTypeEnum.getCode() : GameConstant.TmpFileTypeEnum.UNKNOWN.getCode());
                    newTmpFile.setMd5(updateFile.getMd5());
                    newTmpFile.setFileSize(byteCount);
                    newTmpFile.setFilePath(updateFile.getFilePath());
                    newTmpFile.setOssObjectKey(objectKey);
                    newTmpFile.setOssUrl(StrUtil.format("https://{}.{}/{}", aliyunOssProperties.getBucketName(), aliyunOssProperties.getEndpoint(), objectKey));
                    newTmpFile.setUpdateTime(now);
                    tmpFileMapper.insert(newTmpFile);
                    continue;
                }

                // 更新文件
                if (!tmpFile.getMd5().equals(updateFile.getMd5())) {
                    log.info("job::更新文件 file={}", updateFile.getFilePath());

                    // 上传文件到 OSS
                    Long byteCount = this.reuploadFile(updateFile, objectKey, downloadTmpFile);

                    // 保存文件信息
                    tmpFile.setFileSize(byteCount);
                    tmpFile.setMd5(updateFile.getMd5());
                    tmpFile.setUpdateTime(now);
                    tmpFileMapper.updateById(tmpFile);
                    continue;
                }

                log.info("job::文件一致 file={}", updateFile.getFilePath());
            } finally {
                FileUtil.del(tmpPath);
            }
        }

        // 移除已经不存在的文件
        Set<String> tmpUpdateFilePathSet = tmpUpdateFileResult.getFiles().stream().map(TmpUpdateFile::getFilePath).collect(Collectors.toSet());
        Set<String> tmpFilePathSet = tmpFileList.stream().map(TmpFile::getFilePath).collect(Collectors.toSet());
        tmpFilePathSet.removeAll(tmpUpdateFilePathSet);
        List<TmpFile> deleteFileList = tmpFileList.stream().filter(item -> tmpFilePathSet.contains(item.getFilePath())).toList();
        if (CollUtil.isNotEmpty(deleteFileList)) {
            for (TmpFile deleteFile : deleteFileList) {
                ossClient.deleteObject(aliyunOssProperties.getBucketName(), deleteFile.getOssObjectKey());
                tmpFileMapper.deleteById(deleteFile.getId());
            }
        }

        log.info("job::更新 TMP 文件结束 cost={}ms", timer.intervalMs());
    }

    private Long reuploadFile(TmpUpdateFile updateFile, String objectKey, File downloadTmpFile) {
        HttpUtil.downloadFile("https://download-new.ets2mp.com/files" + updateFile.getFilePath(), downloadTmpFile);
        ossClient.deleteObject(aliyunOssProperties.getBucketName(), objectKey);
        ossClient.putObject(aliyunOssProperties.getBucketName(), objectKey, downloadTmpFile);
        return FileUtil.size(downloadTmpFile);
    }
}
