package com.simbongsa.group.dto.res;

import com.simbongsa.group_user.entity.GroupUser;
import com.simbongsa.member.entity.Member;
import lombok.Builder;

@Builder
public record GroupMemberRes(
        Long memberId,
        String nickname,
        String profileImg
) {
    public static GroupMemberRes mapGroupUserToMember(GroupUser groupUser) {
        Member member = groupUser.getMember();
        return GroupMemberRes.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .profileImg(member.getProfileImg())
                .build();
    }
}
