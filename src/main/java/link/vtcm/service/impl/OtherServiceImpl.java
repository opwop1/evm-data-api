package link.vtcm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import dev.langchain4j.service.AiServices;
import link.vtcm.ai.ChatModelConfig;
import link.vtcm.ai.ChatModelFactory;
import link.vtcm.ai.TranslatorAssistant;
import link.vtcm.common.constant.CacheConstant;
import link.vtcm.common.constant.Constant;
import link.vtcm.common.constant.LockConstant;
import link.vtcm.common.constant.StatusCodeConstant;
import link.vtcm.common.constant.SysConfigConstant;
import link.vtcm.common.exception.BaseException;
import link.vtcm.common.util.SyncUtil;
import link.vtcm.domain.GameTmpVersion;
import link.vtcm.domain.TmpFile;
import link.vtcm.domain.vo.TmpFileVO;
import link.vtcm.domain.vo.TmpVersionVO;
import link.vtcm.mapper.TmpFileMapper;
import link.vtcm.service.GameTmpVersionService;
import link.vtcm.service.OtherService;
import link.vtcm.service.SysConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

/**
 * 其他
 * @author zhangmj
 * @since 2026/2/2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OtherServiceImpl implements OtherService {
    private final TmpFileMapper tmpFileMapper;
    private final GameTmpVersionService gameTmpVersionService;
    private final ChatModelFactory chatModelFactory;
    private final SysConfigService sysConfigService;
    private final SyncUtil syncUtil;
    private final RedissonClient redissonClient;

    @Override
    public List<TmpFileVO> tmpFileList() {
        List<TmpFile> tmpFileList = tmpFileMapper.selectList(null);
        if (CollUtil.isEmpty(tmpFileList)) {
            return CollUtil.newArrayList();
        }

        return tmpFileList.stream().map(tmpFile -> {
            TmpFileVO vo = new TmpFileVO();
            vo.setType(tmpFile.getType());
            vo.setMd5(tmpFile.getMd5());
            vo.setFileSize(tmpFile.getFileSize());
            vo.setFilePath(tmpFile.getFilePath());
            vo.setDownloadUrl(tmpFile.getOssUrl());
            vo.setUpdateTime(tmpFile.getUpdateTime());
            return vo;
        }).toList();
    }

    @Override
    public TmpVersionVO tmpVersion() {
        GameTmpVersion gameTmpVersion = gameTmpVersionService.get();
        if (gameTmpVersion == null) {
            throw new BaseException("获取 TMP 版本信息失败");
        }

        TmpVersionVO vo = new TmpVersionVO();
        BeanUtils.copyProperties(gameTmpVersion, vo);
        return vo;
    }

    @Override
    public String translation(Integer sceneType, String text) {
        String cacheKey = CacheConstant.KEY_TRANS + ObjUtil.defaultIfNull(sceneType, 0) + StringPool.UNDERSCORE + SecureUtil.md5(text);
        RBucket<String> bucket = redissonClient.getBucket(cacheKey);
        String cached = bucket.get();
        if (cached != null) {
            return cached;
        }

        String lockKey = LockConstant.TRANS_LOCK + ObjUtil.defaultIfNull(sceneType, 0) + StringPool.UNDERSCORE + SecureUtil.md5(text);
        return syncUtil.lock(lockKey, 6000L, () -> {
            String result = bucket.get();
            if (result != null) {
                return result;
            }

            String modelName = sysConfigService.getValue(SysConfigConstant.AI_TRANSLATION_MODEL);
            String onlyProvider = sysConfigService.getValue(SysConfigConstant.AI_TRANSLATION_ONLY_PROVIDER);
            String systemPrompt = sysConfigService.getValue(SysConfigConstant.AI_TRANSLATION_SYSTEM_PROMPT);

            Constant.TransSceneTypeEnum sceneEnum = Constant.TransSceneTypeEnum.fromCode(sceneType);
            if (sceneEnum != null) {
                String scenePrompt = sysConfigService.getValue(sceneEnum.getPromptKey());
                if (StrUtil.isNotEmpty(scenePrompt)) {
                    systemPrompt = systemPrompt + "\n\n" + scenePrompt;
                }
            }

            TranslatorAssistant assistant = AiServices.builder(TranslatorAssistant.class)
                .chatModel(chatModelFactory.build(ChatModelConfig.builder()
                    .modelName(modelName)
                    .onlyProviderList(StrUtil.isNotEmpty(onlyProvider) ? CollUtil.newArrayList(onlyProvider.split(StringPool.COMMA)) : null)
                    .reasoningEffort("none")
                    .temperature(0.5)
                    .build()))
                .build();
            try {
                String trans = assistant.trans(systemPrompt, text);
                if (StrUtil.contains(trans, "<not/>")) {
                    trans = text;
                }
                bucket.set(trans, Duration.ofHours(24));
                return trans;
            } catch (Exception e) {
                log.info("translation::接口调用异常", e);
                throw new BaseException(StatusCodeConstant.TRANS_PROVIDER_ERROR, "翻译服务异常，请稍后重试");
            }
        });
    }
}
