package com.simbongsa.feed.dto.req;

import java.util.List;

public record FeedUpdateReq(
        String title,
        String body,
        List<String> images
) {
}
