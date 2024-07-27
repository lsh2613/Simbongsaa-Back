package com.simbongsa.follows_requests.dto.res;

import com.simbongsa.follows_requests.entity.FollowsRequests;
import com.simbongsa.member.entity.Member;
import lombok.Builder;

@Builder
public record FollowsRequestsRes(
        Long followsRequestId,
        Long memberId,
        String nickname,
        String profileImg
        ) {
    public static FollowsRequestsRes mapFollowingMemberToRequestsRes(FollowsRequests followsRequests) {
        Member followingMember = followsRequests.getFollowingMember();
        return FollowsRequestsRes.builder()
                .followsRequestId(followsRequests.getId())
                .memberId(followingMember.getId())
                .nickname(followingMember.getNickname())
                .profileImg(followingMember.getProfileImg())
                .build();
    }

    public static FollowsRequestsRes mapFollowedMemberToRequestsRes(FollowsRequests followsRequests) {
        Member followedMember = followsRequests.getFollowedMember();
        return FollowsRequestsRes.builder()
                .followsRequestId(followsRequests.getId())
                .memberId(followedMember.getId())
                .nickname(followedMember.getNickname())
                .profileImg(followedMember.getProfileImg())
                .build();
    }
}
