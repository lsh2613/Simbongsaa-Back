package com.simbongsa.group.dto.req;

import com.simbongsa.global.common.constant.GroupStatus;
import com.simbongsa.group.entity.Group;
import com.simbongsa.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record GroupCreateReq(
        @NotBlank
        String name,
        String introduction,
        @NotNull
        Integer maxPeople,
        @NotNull
        GroupStatus groupStatus
){
    public Group mapCreateReqToGroup(Member member) {
        return Group.builder()
                .name(name)
                .introduction(introduction)
                .maxPeople(maxPeople)
                .currentPeople(1)
                .groupStatus(groupStatus)
                .groupLeader(member)
                .build();
    }
}
