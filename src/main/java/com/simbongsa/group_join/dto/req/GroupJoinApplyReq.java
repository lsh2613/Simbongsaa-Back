package com.simbongsa.group_join.dto.req;

import com.simbongsa.global.common.constant.JoinType;
import jakarta.validation.constraints.NotNull;

public record GroupJoinApplyReq(
        @NotNull
        Long groupId,
        @NotNull
        JoinType joinType
) {
}
