package com.simbongsa.feed.service;

import com.simbongsa.feed.dto.req.FeedCreateReq;
import com.simbongsa.feed.dto.req.FeedUpdateReq;
import com.simbongsa.feed.dto.res.FeedPageRes;
import com.simbongsa.feed.dto.res.FeedRes;
import com.simbongsa.feed.dto.res.MyFeedRes;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedService {
    Long createFeed(Long memberId, FeedCreateReq feedCreateReq);
    FeedRes getFeed(Long memberId, Long feedId);
    FeedPageRes getFeeds(Long memberId, Long lastFeedId, Pageable pageable);
    void updateFeed(Long memberId, Long feedId, FeedUpdateReq feedUpdateReq);
    void deleteFeed(Long memberId, Long feedId);
    void likeFeed(Long feedId);
    FeedPageRes getMyFeeds(Long memberId, Long lastFeedId, Pageable pageable);
    MyFeedRes getMyFeed(Long memberId, Long feedId);
}
