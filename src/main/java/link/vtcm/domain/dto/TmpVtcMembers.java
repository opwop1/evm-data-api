package link.vtcm.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * TMP接口-VTC成员信息
 * @author zhangmj
 * @since 2025/1/2
 */
@Data
public class TmpVtcMembers {
    /**
     * 所有成员
     */
    private List<TmpVtcMember> members;

    /**
     * 成员数量
     */
    @JsonProperty("members_count")
    private Integer membersCount;
}
