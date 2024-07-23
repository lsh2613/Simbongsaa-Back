package com.simbongsa.feed.repository;

import com.simbongsa.feed.entity.Feed;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface FeedRepositoryCustom {
    Slice<Feed> getFeeds(Long memberId, Long lastFeedId, Pageable pageable);
}
