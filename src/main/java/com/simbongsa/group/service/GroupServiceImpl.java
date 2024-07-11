package com.simbongsa.group.service;

import com.simbongsa.global.EntityFacade;
import com.simbongsa.global.common.apiPayload.code.statusEnums.ErrorStatus;
import com.simbongsa.global.common.exception.GeneralHandler;
import com.simbongsa.group.dto.req.GroupCreateReq;
import com.simbongsa.group.dto.req.GroupSearchReq;
import com.simbongsa.group.dto.req.GroupUpdateReq;
import com.simbongsa.group.dto.res.GroupRes;
import com.simbongsa.group.dto.res.GroupSearchRes;
import com.simbongsa.group.entity.Group;
import com.simbongsa.group.repository.GroupRepository;
import com.simbongsa.group.repository.GroupRepositoryCustom;
import com.simbongsa.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class GroupServiceImpl implements GroupService{
    private final EntityFacade entityFacade;
    private final GroupRepository groupRepository;
    private final GroupRepositoryCustom groupRepositoryCustom;

    @Override
    public GroupRes getGroup(Long memberId, Long groupId) {
        Group group = entityFacade.getGroup(groupId);
        return GroupRes.mapEntityToRes(group, memberId);
    }

    @Override
    public GroupSearchRes getGroupList(Long memberId, Pageable pageable, GroupSearchReq groupSearchReq) {
        //TODO: groupSearchReq는 일단 keyword로만 search
        // offset limit 방식을 활용하여 response

        Page<Group> groups = groupRepositoryCustom.searchGroup(pageable, groupSearchReq);
        return new GroupSearchRes(groups.getContent(), pageable.getPageNumber(), groups.hasNext());
    }

    @Override
    public Long createGroup(Long memberId, GroupCreateReq groupCreateReq) {
        Member member = entityFacade.getMember(memberId);
        Group group = groupCreateReq.mapCreateReqToGroup(member);
        Group saveGroup = groupRepository.save(group);
        //GroupUser에 방장 값을 넣어줘야 하는가?

        return saveGroup.getId();
    }

    @Override
    public void updateGroup(Long memberId, Long groupId, GroupUpdateReq groupUpdateReq) {
        Group group = entityFacade.getGroup(groupId);

        //권한 검사
        checkMemberRole(group.getGroupLeader().getId(), memberId);

        //업데이트
        if (groupUpdateReq.name() != null)
            group.setName(groupUpdateReq.name());
        if (groupUpdateReq.introduction() != null)
            group.setIntroduction(groupUpdateReq.introduction());
        if (groupUpdateReq.maxPeople() != null) {
            if (group.getCurrentPeople() > groupUpdateReq.maxPeople())
                throw new GeneralHandler(ErrorStatus.GROUP_FULL);
            group.setMaxPeople(groupUpdateReq.maxPeople());
        }
        if (groupUpdateReq.groupStatus() != null)
            group.setGroupStatus(groupUpdateReq.groupStatus());
    }

    @Override
    public void deleteGroup(Long memberId, Long groupId) {
        Group group = entityFacade.getGroup(groupId);
        checkMemberRole(group.getGroupLeader().getId(), memberId);
        groupRepository.delete(group);
    }

    private void checkMemberRole(Long leaderId, Long memberId) {
        if (!leaderId.equals(memberId))
            throw new GeneralHandler(ErrorStatus.USER_FORBIDDEN);
    }
}
