package com.simbongsa.feed.dto.res;

import com.simbongsa.feed.entity.Feed;
import com.simbongsa.global.image.entity.Image;
import lombok.Builder;

import java.util.List;

@Builder
public record FeedRes(
        Long id,
        String title,
        String body,
        Integer views,
        Integer likes,
        List<Image> images
) {
    public static FeedRes mapEntityToRes(Feed feed) {
        return FeedRes.builder()
                .id(feed.getId())
                .title(feed.getTitle())
                .body(feed.getBody())
                .views(feed.getViews())
                .likes(feed.getLikes())
                .images(feed.getImages())
                .build();
    }
}
