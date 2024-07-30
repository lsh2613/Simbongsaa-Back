package com.simbongsa.follows.dto.res;

import com.simbongsa.follows.entity.Follows;
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
                .lastFollowsRequestId(getLastId(myFollowingPage.getContent()))
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
                .lastFollowsRequestId(getLastId(myFollowerPage.getContent()))
                .hasNext(myFollowerPage.hasNext())
                .build();
    }

    private static Long getLastId(List<Follows> followsList) {
        return followsList.isEmpty() ? null : followsList.get(followsList.size() - 1).getId();
    }
}
