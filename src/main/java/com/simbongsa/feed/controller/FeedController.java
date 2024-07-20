package com.simbongsa.feed.controller;

import com.simbongsa.feed.dto.req.FeedCreateReq;
import com.simbongsa.feed.dto.req.FeedUpdateReq;
import com.simbongsa.feed.dto.res.FeedPageRes;
import com.simbongsa.feed.dto.res.FeedRes;
import com.simbongsa.feed.dto.res.MyFeedRes;
import com.simbongsa.feed.service.FeedService;
import com.simbongsa.global.common.apiPayload.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/members/{memberId}")
public class FeedController {
    private final FeedService feedService;

    /**
     * 피드 생성
     * @param memberId 생상자 Id
     * @param feedCreateReq 피드 정보 Dto
     * @return 피드 Id
     */
    @PostMapping("/feeds")
    public CustomApiResponse<Long> createFeed(@AuthenticationPrincipal Long memberId,
                                              @RequestBody FeedCreateReq feedCreateReq) {
        Long feedId = feedService.createFeed(memberId, feedCreateReq);
        return CustomApiResponse.onSuccess(feedId);
    }

    /**
     * 피드 상세 조회
     * @param memberId 조회자 Id
     * @param feedId 피드 Id
     * @return 피드 Info
     */
    @GetMapping("/feeds/{feedId}")
    public CustomApiResponse<FeedRes> getFeed(@PathVariable Long memberId,
                                              @PathVariable Long feedId) {
        FeedRes feed = feedService.getFeed(memberId, feedId);
        return CustomApiResponse.onSuccess(feed);
    }

    /**
     * 피드 리스트 조회
     * @param memberId 조회자 Id
     * @return 피드 List Info
     */
    @GetMapping("/feeds")
    public CustomApiResponse<FeedPageRes> getFeeds(@AuthenticationPrincipal Long memberId,
                                                   @PageableDefault Pageable pageable,
                                                   @RequestParam Long lastFeedId
                                                     ) {
        FeedPageRes feeds = feedService.getFeeds(memberId, lastFeedId, pageable);
        return CustomApiResponse.onSuccess(feeds);
    }

    /**
     * 피드 수정
     * @param memberId 피드 admin Id
     * @param feedId 피드 id
     * @param feedUpdateReq 업데이트 내용 Dto
     * @return ?
     */
    @PatchMapping("/feeds/{feedId}")
    public CustomApiResponse<?> update(@AuthenticationPrincipal Long memberId,
                                          @PathVariable Long feedId,
                                          @RequestBody FeedUpdateReq feedUpdateReq
                                          ) {
        feedService.updateFeed(memberId, feedId, feedUpdateReq);
        return CustomApiResponse.onSuccess();
    }

    /**
     * 피드 삭제
     * @param memberId 피드 작성자 Id
     * @param feedId 피드 Id
     * @return ?
     */
    @DeleteMapping("/feeds/{feedId}")
    public CustomApiResponse<?> deleteFeed(@AuthenticationPrincipal Long memberId,
                                           @PathVariable Long feedId) {
        feedService.deleteFeed(memberId, feedId);
        return CustomApiResponse.onSuccess();
    }

    /**
     * 내 피드 리스트 조회
     * @param memberId 사용자 Id
     * @return 내 피드 리스트
     */
    @GetMapping("/my-feeds")
    public CustomApiResponse<FeedPageRes> getMyFeeds(@AuthenticationPrincipal Long memberId,
                                                         @PageableDefault Pageable pageable,
                                                         @RequestParam Long lastFeedId) {
        FeedPageRes myFeeds = feedService.getMyFeeds(memberId, lastFeedId, pageable);
        return CustomApiResponse.onSuccess(myFeeds);
    }

    /**
     * 내 피드 상세조회
     * @param memberId 사용자 Id
     * @param feedId 피드 Id
     * @return 내 피드 상세정보
     */
    @GetMapping("/my-feeds/{feedId}")
    public CustomApiResponse<MyFeedRes> getMyFeed(@AuthenticationPrincipal Long memberId,
                                                  @PathVariable Long feedId) {
        MyFeedRes myFeed = feedService.getMyFeed(memberId, feedId);
        return CustomApiResponse.onSuccess(myFeed);
    }

    /**
     * 피드 좋아요
     * @param feedId 피드 Id
     * @return ?
     */
    @PostMapping("/feeds/{feedId}/like")
    public CustomApiResponse<?> likeFeed(@PathVariable Long feedId) {
        feedService.likeFeed(feedId);
        return CustomApiResponse.onSuccess();
    }
}
