package com.simbongsa.group_join.dto.res;

import com.simbongsa.group_join.entity.GroupJoin;
import com.simbongsa.member.entity.Member;
import lombok.Builder;

@Builder
public record GroupJoinRes(
        Long groupJoinId,
        Long memberId,
        String nickname,
        String introduction,
        String profileImg
) {

    public static GroupJoinRes mapMemberToJoinRes(GroupJoin groupJoin) {
        Member member = groupJoin.getMember();
        return GroupJoinRes.builder()
                .groupJoinId(groupJoin.getId())
                .memberId(member.getId())
                .nickname(member.getNickname())
                .introduction(member.getIntroduction())
                .profileImg(member.getProfileImg())
                .build();
    }
}
