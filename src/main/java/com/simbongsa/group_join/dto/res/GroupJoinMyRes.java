package com.simbongsa.group_join.dto.res;

import com.simbongsa.group.entity.Group;
import com.simbongsa.group_join.entity.GroupJoin;
import lombok.Builder;

@Builder
public record GroupJoinMyRes(
        Long groupJoinId,
        Long groupId,
        String name
) {

    public static GroupJoinMyRes mapGroupJoinToMyRes(GroupJoin groupJoin) {
        Group group = groupJoin.getGroup();
        return GroupJoinMyRes.builder()
                .groupJoinId(groupJoin.getId())
                .groupId(group.getId())
                .name(group.getName())
                .build();
    }
}
