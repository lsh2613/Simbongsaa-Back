package com.simbongsa.group.dto.res;

import com.simbongsa.group.entity.Group;

import java.util.List;

public record GroupSearchRes(
        List<Group> groupList,
        Integer lastPage,
        Boolean hasNext
) {
}
