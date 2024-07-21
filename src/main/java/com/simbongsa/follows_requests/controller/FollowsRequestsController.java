package com.simbongsa.follows_requests.controller;

import com.simbongsa.follows_requests.dto.req.FollowsRequestsDecideReq;
import com.simbongsa.follows_requests.dto.res.FollowsRequestsRes;
import com.simbongsa.follows_requests.service.FollowsRequestsService;
import com.simbongsa.global.common.apiPayload.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/follows-requests")
public class FollowsRequestsController {

    private final FollowsRequestsService followsRequestsService;

    /**
     * 팔로우
     * @param loginId 신청자 id
     * @param followedMemberId 팔로우 받는 사용자 id
     * @return null
     */
    @PostMapping("/{followedMemberId}")
    public CustomApiResponse follow(@AuthenticationPrincipal Long loginId,
                                    @PathVariable("followedMemberId") Long followedMemberId) {
        followsRequestsService.follow(loginId, followedMemberId);

        return CustomApiResponse.onSuccess();
    }
}
