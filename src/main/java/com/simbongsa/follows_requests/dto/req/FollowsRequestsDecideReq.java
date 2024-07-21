package com.simbongsa.follows_requests.dto.req;

import com.simbongsa.global.common.constant.FollowsRequestsDecide;
import jakarta.validation.constraints.NotNull;

public record FollowsRequestsDecideReq(
        @NotNull
        Long followsRequestsId,
        @NotNull
        FollowsRequestsDecide followRequestsDecide
) {
}
