package com.simbongsa.follows.dto.res;

import com.simbongsa.follows.entity.Follows;
import com.simbongsa.member.entity.Member;
import lombok.Builder;

@Builder
public record FollowsRes(
        Long followsId,
        Long memberId,
        String nickname,
        String profileImg
) {
    public static FollowsRes mapFollowingToFollowsRes(Follows follows) {
        Member followedMember = follows.getFollowedMember();
        return FollowsRes.builder()
                .followsId(follows.getId())
                .memberId(followedMember.getId())
                .nickname(followedMember.getNickname())
                .profileImg(followedMember.getProfileImg())
                .build();
    }

    public static FollowsRes mapFollowerToFollowsRes(Follows follows) {
        Member getFollowingMember = follows.getFollowingMember();
        return FollowsRes.builder()
                .followsId(follows.getId())
                .memberId(getFollowingMember.getId())
                .nickname(getFollowingMember.getNickname())
                .profileImg(getFollowingMember.getProfileImg())
                .build();
    }
}
