package link.vtcm.service.impl;

import link.vtcm.domain.GameTmpVersion;
import link.vtcm.mapper.GameTmpVersionMapper;
import link.vtcm.service.GameTmpVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * TMP 版本信息
 * @author zhangmj
 * @since 2026/4/20
 */
@Service
@RequiredArgsConstructor
public class GameTmpVersionServiceImpl implements GameTmpVersionService {
    private final GameTmpVersionMapper gameTmpVersionMapper;

    @Override
    public void set(GameTmpVersion entity) {
        Long count = gameTmpVersionMapper.selectCount(null);
        if (count == 0) {
            gameTmpVersionMapper.insert(entity);
        } else {
            gameTmpVersionMapper.update(entity, null);
        }
    }

    @Override
    public GameTmpVersion get() {
        return gameTmpVersionMapper.selectOne(null);
    }
}
