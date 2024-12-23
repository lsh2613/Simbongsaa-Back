package com.simbongsa.follows_requests.controller;

import com.simbongsa.follows_requests.dto.res.FollowsRequestsPageRes;
import com.simbongsa.follows_requests.service.FollowsRequestsService;
import com.simbongsa.global.common.apiPayload.CustomApiResponse;
import com.simbongsa.global.common.constant.FollowsRequestsDecide;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 팔로우 요청에 대한 수락/거절
     * @param followsRequestsId 결정(수락/거절)할 팔로우 요청 id
     * @param followRequestsDecide 수락/거절
     * @return null
     */
    @PostMapping("/{followsRequestsId}/decision")
    public CustomApiResponse decideRequests(@AuthenticationPrincipal Long loginId,
                                            @PathVariable Long followsRequestsId,
                                            @RequestParam FollowsRequestsDecide followRequestsDecide) {
        followsRequestsService.decideRequests(loginId, followsRequestsId, followRequestsDecide);
        return CustomApiResponse.onSuccess();
    }

    /**
     * 내가 받은 팔로우 요청 리스트 조회
     * @param loginId 유저id
     * @return List<FollowsRequestsPageRes> 내가 받은 팔로우 요청 리스트
     */
    @GetMapping("/received-requests")
    public CustomApiResponse getReceivedFollowRequests(@AuthenticationPrincipal Long loginId,
                                                       @PageableDefault Pageable pageable,
                                                       @RequestParam Long lastFollowsRequestId) {
        FollowsRequestsPageRes receivedFollowsRequestsPage = followsRequestsService.getReceivedFollowsRequestsPage(loginId, lastFollowsRequestId, pageable);
        return CustomApiResponse.onSuccess(receivedFollowsRequestsPage);
    }

    /**
     * 내가 신청한 팔로우 요청 조회
     * @param loginId 유저 id
     * @return List<FollowsRequestsPageRes> 내가 신청한 팔로우 요청 리스트
     */
    @GetMapping("/sent-requests")
    public CustomApiResponse getSentFollowsRequests(@AuthenticationPrincipal Long loginId,
                                                   @PageableDefault Pageable pageable,
                                                   @RequestParam Long lastFollowsRequestId) {
        FollowsRequestsPageRes sentFollowsRequestsPage = followsRequestsService.getSentFollowsRequestsPage(loginId, lastFollowsRequestId, pageable);
        return CustomApiResponse.onSuccess(sentFollowsRequestsPage);
    }

    /**
     * 팔로우 요청 삭제
     * @param loginId 유저id
     * @param followsRequestsId 삭제할 팔로우 요청 id
     * @return null
     */
    @DeleteMapping("/{followsRequestsId}")
    public CustomApiResponse deleteFollowsRequests(@AuthenticationPrincipal Long loginId,
                                                   @PathVariable Long followsRequestsId) {
        followsRequestsService.delete(loginId, followsRequestsId);
        return CustomApiResponse.onSuccess();
    }
}
