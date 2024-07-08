package com.simbongsa.group.dto.req;

import com.simbongsa.global.common.constant.GroupStatus;

public record GroupUpdateReq(
        String name,
        String introduction,
        Integer maxPeople,
        GroupStatus groupStatus
) {
}
