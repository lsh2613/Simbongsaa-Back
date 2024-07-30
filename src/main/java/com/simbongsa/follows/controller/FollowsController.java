package com.simbongsa.follows.controller;

import com.simbongsa.follows.service.FollowsService;
import com.simbongsa.global.common.apiPayload.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/follows")
public class FollowsController {
    private final FollowsService followsService;

    /**
     * 내가 팔로우하고 있는 데이터를 삭제
     * @param loginId
     * @param myFollowsId
     * @return
     */
    @DeleteMapping("/{myFollowsId}")
    public CustomApiResponse deleteMyFollows(@AuthenticationPrincipal Long loginId,
                                             @PathVariable Long myFollowsId) {
        followsService.deleteMyFollows(loginId, myFollowsId);
        return CustomApiResponse.onSuccess();
    }

    /**
     * 나를 팔로우하는 상대방의 데이터를 삭제
     * @param loginId
     * @param oppositeFollowsId
     * @return
     */
    @DeleteMapping("/{oppositeFollowsId}")
    public CustomApiResponse deleteOppositeFollows(@AuthenticationPrincipal Long loginId,
                                                   @PathVariable Long oppositeFollowsId) {
        followsService.deleteOppositeFollows(loginId, oppositeFollowsId);
        return CustomApiResponse.onSuccess();
    }

}
