package link.vtcm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import link.vtcm.domain.SteamDlc;
import link.vtcm.domain.param.DlcListParam;
import link.vtcm.domain.vo.DlcVO;
import link.vtcm.mapper.SteamDlcMapper;
import link.vtcm.service.SteamDlcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SteamDLC
 * @author zhangmj
 * @since 2025/7/1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SteamDlcServiceImpl implements SteamDlcService {
    private final SteamDlcMapper baseMapper;

    @Override
    public List<DlcVO> list(DlcListParam param) {
        // 查询数据
        List<SteamDlc> steamDlcList = baseMapper.selectList(new LambdaQueryWrapper<SteamDlc>().eq(param.getType() != null, SteamDlc::getType, param.getType()));

        // 转为VO返回
        return steamDlcList.stream().map(item -> {
            DlcVO vo = new DlcVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).toList();
    }
}
