package com.simbongsa.group_join.dto.req;

import com.simbongsa.global.common.constant.JoinDecide;
import jakarta.validation.constraints.NotNull;

public record GroupJoinDecideReq(
        @NotNull
        Long groupJoinId,
        @NotNull
        JoinDecide joinDecide
) {
}
