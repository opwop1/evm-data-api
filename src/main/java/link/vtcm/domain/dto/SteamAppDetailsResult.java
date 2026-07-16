package link.vtcm.domain.dto;

import lombok.Data;

/**
 * Steam游戏详情返回
 * @author zhangmj
 * @since 2025/6/30
 */
@Data
public class SteamAppDetailsResult<T> {
    private Boolean success;
    private T data;
}
