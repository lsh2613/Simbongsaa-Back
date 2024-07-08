package com.simbongsa.group.service;

import com.simbongsa.group.dto.req.GroupCreateReq;
import com.simbongsa.group.dto.req.GroupSearchReq;
import com.simbongsa.group.dto.req.GroupUpdateReq;
import com.simbongsa.group.dto.res.GroupRes;
import com.simbongsa.group.dto.res.GroupSearchRes;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroupService {
    GroupRes getGroup(Long memberId, Long groupId);
    GroupSearchRes getGroupList(Long memberId, Pageable pageable, GroupSearchReq groupSearchReq);
    Long createGroup(Long memberId, GroupCreateReq groupCreateReq);
    void updateGroup(Long memberId, Long groupId, GroupUpdateReq groupUpdateReq);

    void deleteGroup(Long memberId, Long groupId);
}
