package com.simbongsa.follows.controller;

import com.simbongsa.follows.dto.res.FollowsPageRes;
import com.simbongsa.follows.service.FollowsService;
import com.simbongsa.global.common.apiPayload.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    /**
     * 나의 팔로잉 리스트 조회
     * @param loginId 로그인 유저
     * @return 팔로잉 리스트
     */
    @GetMapping("/following")
    public CustomApiResponse getMyFollowing(@AuthenticationPrincipal Long loginId,
                                            @RequestParam Long lastFollowsId,
                                            @PageableDefault Pageable pageable) {
        FollowsPageRes followingPageRes = followsService.getMyFollowingPage(loginId, lastFollowsId, pageable);
        return CustomApiResponse.onSuccess(followingPageRes);
    }

    /**
     * 타 유저의 팔로잉 리스트 조회
     * @param loginId  로그인 유저
     * @param memberId 조회하려는 유저
     * @return 타 사용자의 팔로잉 리스트
     */
    @GetMapping("/following/members/{memberId}")
    public CustomApiResponse getMemberFollowing(@AuthenticationPrincipal Long loginId,
                                              @PathVariable Long memberId,
                                              @RequestParam Long lastFollowsId,
                                              @PageableDefault Pageable pageable) {
        FollowsPageRes followingPageRes = followsService.getMemberFollowingPage(loginId, memberId, lastFollowsId, pageable);
        return CustomApiResponse.onSuccess(followingPageRes);
    }

    /**
     * 나의 팔로워 리스트 조회
     * @param loginId 로그인 유저
     * @return 팔로워 리스트
     */
    @GetMapping("/follower")
    public CustomApiResponse getMyFollower(@AuthenticationPrincipal Long loginId,
                                           @RequestParam Long lastFollowsId,
                                           @PageableDefault Pageable pageable) {
        FollowsPageRes followerPageRes = followsService.getMyFollowerPage(loginId, lastFollowsId, pageable);
        return CustomApiResponse.onSuccess(followerPageRes);
    }

    /**
     * 타 유저의 팔로워 리스트 조회
     * @param loginId 로그인 유저
     * @param memberId 조회하려는 유저
     * @return 팔로워 리스트
     */
    @GetMapping("/follower/member/{memberId}")
    public CustomApiResponse getMemberFollower(@AuthenticationPrincipal Long loginId,
                                               @PathVariable Long memberId,
                                               @RequestParam Long lastFollowsId,
                                               @PageableDefault Pageable pageable) {
        FollowsPageRes followerPageRes = followsService.getMemberFollowerPage(loginId, memberId,lastFollowsId, pageable);
        return CustomApiResponse.onSuccess(followerPageRes);
    }

}
