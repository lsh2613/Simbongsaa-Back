package com.simbongsa.group_join.service;

import com.simbongsa.group_join.dto.req.GroupJoinApplyReq;
import com.simbongsa.group_join.dto.req.GroupJoinDecideReq;
import com.simbongsa.group_join.dto.res.GroupJoinMyRes;
import com.simbongsa.group_join.dto.res.GroupJoinRes;

import java.util.List;

public interface GroupJoinService {
    void applyJoin(Long memberId, GroupJoinApplyReq groupJoinApplyReq);

    void decideJoin(Long memberId, GroupJoinDecideReq groupJoinDecideReq);

    List<GroupJoinRes> getGroupJoinList(Long memberId, Long groupId);

    List<GroupJoinMyRes> getMyGroupJoinList(Long memberId);
}
