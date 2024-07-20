package com.simbongsa.feed.dto.req;

import com.simbongsa.feed.entity.Feed;
import com.simbongsa.member.entity.Member;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record FeedCreateReq(
        @NotBlank
        String title,
        String body,
        List<String> images
) {

    public Feed mapCreateReqToFeed(Member member) {
        return Feed.builder()
                .title(title)
                .body(body)
                .likes(0)
                .views(0)
                .member(member)
                .build();
    }
}
