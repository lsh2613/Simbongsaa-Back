package com.simbongsa.follows_requests.dto.res;

import com.simbongsa.follows_requests.entity.FollowsRequests;
import com.simbongsa.global.util.PageUtil;
import lombok.Builder;
import org.springframework.data.domain.Slice;

import java.util.List;

@Builder
public record FollowsRequestsPageRes(
        List<FollowsRequestsRes> followsRequestsResList,
        Long lastFollowsRequestId,
        Boolean hasNext
) {
    public static FollowsRequestsPageRes mapSentRequestsToPageRes(Slice<FollowsRequests> slice) {
        return FollowsRequestsPageRes.builder()
                .followsRequestsResList(
                        slice.stream()
                                // 보낸 팔로우-요청 반환 -> FollowsRequests에 FollowedMember 담김 -> mapFollowedMemberToRequestsRes()
                                .map(FollowsRequestsRes::mapFollowedMemberToRequestsRes)
                                .toList()
                )
                .lastFollowsRequestId(PageUtil.getLastElement(slice.getContent()).getId())
                .hasNext(slice.hasNext())
                .build();
    }

    public static FollowsRequestsPageRes mapReceivedRequestsToPageRes(Slice<FollowsRequests> slice) {
        return FollowsRequestsPageRes.builder()
                .followsRequestsResList(
                        slice.stream()
                                // 받은 팔로우-요청 반환 -> FollowsRequests에 FollowingMember 담김 -> mapFollowingMemberToRequestsRes()
                                .map(FollowsRequestsRes::mapFollowingMemberToRequestsRes)
                                .toList()
                )
                .lastFollowsRequestId(PageUtil.getLastElement(slice.getContent()).getId())
                .hasNext(slice.hasNext())
                .build();
    }

}
