package com.simbongsa.feed.service;

import com.simbongsa.feed.dto.req.FeedCreateReq;
import com.simbongsa.feed.dto.req.FeedUpdateReq;
import com.simbongsa.feed.dto.res.FeedPageRes;
import com.simbongsa.feed.dto.res.FeedRes;
import com.simbongsa.feed.dto.res.MyFeedRes;
import com.simbongsa.feed.entity.Feed;
import com.simbongsa.feed.repository.FeedRepository;
import com.simbongsa.feed.repository.FeedRepositoryCustom;
import com.simbongsa.global.EntityFacade;
import com.simbongsa.global.common.apiPayload.code.statusEnums.ErrorStatus;
import com.simbongsa.global.common.constant.ImageType;
import com.simbongsa.global.common.exception.GeneralHandler;
import com.simbongsa.global.image.entity.Image;
import com.simbongsa.global.image.service.ImageService;
import com.simbongsa.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class FeedServiceImpl implements FeedService{
    private final EntityFacade entityFacade;
    private final FeedRepository feedRepository;
    private final FeedRepositoryCustom feedRepositoryCustom;
    private final ImageService imageService;

    @Override
    public Long createFeed(Long memberId, FeedCreateReq feedCreateReq) {
        Member member = entityFacade.getMember(memberId);
        Feed feed = feedCreateReq.mapCreateReqToFeed(member);

        // 이미지를 생성하고 Feed에 추가
        List<Image> images = imageService.saveImages(ImageType.FEED, feed, feedCreateReq.images());
        feed.addImages(images);

        member.addFeed(feed);

        Feed saveFeed = feedRepository.save(feed);
        return saveFeed.getId();
    }

    @Override
    public FeedRes getFeed(Long memberId, Long feedId) {
        Feed feed = entityFacade.getFeed(feedId);
        feedRepository.increaseViews(feedId);
        return FeedRes.mapEntityToRes(feed);
    }

    @Override
    public FeedPageRes getFeeds(Long memberId, Long lastFeedId, Pageable pageable) {
        Slice<Feed> feeds = feedRepositoryCustom.getFeeds(memberId, lastFeedId, pageable);
        return FeedPageRes.mapSliceToPageRes(feeds, Optional.ofNullable(memberId));
    }

    @Override
    public void updateFeed(Long memberId, Long feedId, FeedUpdateReq feedUpdateReq) {
        Feed feed = checkMemberRole(memberId, feedId);

        if (nullCheck(feedUpdateReq.title()))
            feed.setTitle(feedUpdateReq.title());
        if (nullCheck(feedUpdateReq.body()))
            feed.setBody(feedUpdateReq.body());

        // 기존 피드의 이미지 리스트 가져오기
        List<Image> existingImages = feed.getImages();

        // 이미지 리스트 비교를 통해 추가 및 삭제 구분
        List<String> imagesToAdd = feedUpdateReq.images().stream()
                    .filter(image -> !existingImages.contains(image))
                    .toList();
        List<Image> images = imageService.saveImages(ImageType.FEED, feed, imagesToAdd);

        List<Image> imagesToRemove = existingImages.stream()
                .filter(image -> !feedUpdateReq.images().contains(image))
                .collect(Collectors.toList());

        // 추가할 이미지 처리
        feed.getImages().addAll(images);

        // 삭제할 이미지 처리
        feed.getImages().removeAll(imagesToRemove);
    }

    @Override
    public void deleteFeed(Long memberId, Long feedId) {
        Feed feed = checkMemberRole(memberId, feedId);

        feedRepository.delete(feed);
    }

    @Override
    public void likeFeed(Long feedId) {
        feedRepository.increaseLikes(feedId);
    }

    @Override
    public FeedPageRes getMyFeeds(Long memberId, Long lastFeedId, Pageable pageable) {
        Slice<Feed> feeds = feedRepositoryCustom.getFeeds(memberId, lastFeedId, pageable);
        return FeedPageRes.mapSliceToPageRes(feeds, Optional.ofNullable(memberId));
    }

    @Override
    public MyFeedRes getMyFeed(Long memberId, Long feedId) {
        Feed feed = checkMemberRole(memberId, feedId);
        return MyFeedRes.mapEntityToRes(feed);
    }

    private Feed checkMemberRole(Long memberId, Long feedId) {
        Feed feed = entityFacade.getFeed(feedId);
        if (!feed.getMember().getId().equals(memberId))
            throw new GeneralHandler(ErrorStatus.USER_FORBIDDEN);
        return feed;
    }

    private boolean nullCheck(Object o) {
        return o != null;
    }


}
