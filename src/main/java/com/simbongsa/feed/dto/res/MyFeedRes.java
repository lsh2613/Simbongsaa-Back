package com.simbongsa.feed.dto.res;

import com.simbongsa.feed.entity.Feed;
import com.simbongsa.global.image.entity.Image;
import lombok.Builder;

import java.util.List;

@Builder
public record MyFeedRes(
        Long id,
        String title,
        String body,
        Integer views,
        Integer likes,
        List<Image> images,
        Boolean isHost
) {
    public static MyFeedRes mapEntityToRes(Feed feed) {
        return MyFeedRes.builder()
                .id(feed.getId())
                .title(feed.getTitle())
                .body(feed.getBody())
                .views(feed.getViews())
                .likes(feed.getLikes())
                .images(feed.getImages())
                .isHost(true)
                .build();
    }
}
