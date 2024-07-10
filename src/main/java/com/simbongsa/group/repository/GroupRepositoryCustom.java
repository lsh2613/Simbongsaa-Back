package com.simbongsa.group.repository;

import com.simbongsa.group.dto.req.GroupSearchReq;
import com.simbongsa.group.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GroupRepositoryCustom {
    Page<Group> searchGroup(Pageable pageable, GroupSearchReq groupSearchReq);
}
