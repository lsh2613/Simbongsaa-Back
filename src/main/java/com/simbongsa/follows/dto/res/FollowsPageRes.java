package com.simbongsa.follows.dto.res;

import com.simbongsa.follows.entity.Follows;
import com.simbongsa.global.util.PageUtil;
import lombok.Builder;
import org.springframework.data.domain.Slice;

import java.util.List;

@Builder
public record FollowsPageRes(
        List<FollowsRes> followsResList,
        Long lastFollowsRequestId,
        Boolean hasNext
) {
    public static FollowsPageRes mapFollowingPageToPageRes(Slice<Follows> myFollowingPage) {
        return FollowsPageRes.builder()
                .followsResList(
                        myFollowingPage.stream()
                                .map(FollowsRes::mapFollowingToFollowsRes)
                                .toList()
                )
                .lastFollowsRequestId(PageUtil.getLastElement(myFollowingPage.getContent()).getId())
                .hasNext(myFollowingPage.hasNext())
                .build();
    }

    public static FollowsPageRes mapFollowerPageToPageRes(Slice<Follows> myFollowerPage) {
        return FollowsPageRes.builder()
                .followsResList(
                        myFollowerPage.stream()
                                .map(FollowsRes::mapFollowerToFollowsRes)
                                .toList()
                )
                .lastFollowsRequestId(PageUtil.getLastElement(myFollowerPage.getContent()).getId())
                .hasNext(myFollowerPage.hasNext())
                .build();
    }

}
