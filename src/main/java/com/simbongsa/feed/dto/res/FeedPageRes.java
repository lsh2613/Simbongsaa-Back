package com.simbongsa.feed.dto.res;

import com.simbongsa.feed.entity.Feed;
import lombok.Builder;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

@Builder
public record FeedPageRes(
        List<FeedRes> feedResList,
        Long lastFeedId,
        Boolean hasNext
) {
    public static FeedPageRes mapSliceToPageRes(Slice<Feed> feeds, Optional<Long> memberId) {
        return FeedPageRes.builder()
                .feedResList(feeds.stream().map(feed -> FeedRes.mapEntityToRes(feed)).toList())
                .lastFeedId(getLastId(feeds.getContent()))
                .hasNext(feeds.hasNext())
                .build();
    }

    public static Long getLastId(List<Feed> feeds) {
        return feeds.isEmpty() ? null : feeds.get(feeds.size() - 1).getId();
    }
}
