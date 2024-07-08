package com.simbongsa.group.dto.res;

import com.simbongsa.global.common.constant.GroupStatus;
import com.simbongsa.group.entity.Group;
import lombok.Builder;

@Builder
public record GroupRes(
        Long id,
        String name,
        String introduction,
        Integer maxPeople,
        Integer currentPeople,
        GroupStatus groupStatus,
        Boolean isHost
) {

    public static GroupRes mapEntityToRes(Group group, Long memberId) {
        return GroupRes.builder()
                .id(group.getId())
                .name(group.getName())
                .introduction(group.getIntroduction())
                .maxPeople(group.getMaxPeople())
                .currentPeople(group.getCurrentPeople())
                .groupStatus(group.getGroupStatus())
                .isHost(group.getGroupLeader().getId().equals(memberId))
                .build();
    }
}
