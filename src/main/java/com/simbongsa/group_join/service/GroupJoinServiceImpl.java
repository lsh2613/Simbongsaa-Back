package com.simbongsa.group_join.service;

import com.simbongsa.global.EntityFacade;
import com.simbongsa.global.common.apiPayload.code.statusEnums.ErrorStatus;
import com.simbongsa.global.common.constant.GroupStatus;
import com.simbongsa.global.common.exception.GeneralHandler;
import com.simbongsa.group.entity.Group;
import com.simbongsa.group_join.dto.req.GroupJoinApplyReq;
import com.simbongsa.group_join.dto.req.GroupJoinDecideReq;
import com.simbongsa.group_join.dto.res.GroupJoinMyRes;
import com.simbongsa.group_join.dto.res.GroupJoinRes;
import com.simbongsa.group_join.entity.GroupJoin;
import com.simbongsa.group_join.repository.GroupJoinRepository;
import com.simbongsa.group_user.entity.GroupUser;
import com.simbongsa.group_user.repository.GroupUserRepository;
import com.simbongsa.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class GroupJoinServiceImpl implements GroupJoinService{
    private final EntityFacade entityFacade;
    private final GroupJoinRepository groupJoinRepository;
    private final GroupUserRepository groupUserRepository;
    @Override
    public void applyJoin(Long memberId, GroupJoinApplyReq groupJoinApplyReq) {
        Member member = entityFacade.getMember(memberId);
        Group group = entityFacade.getGroup(groupJoinApplyReq.groupId());

        Optional<GroupJoin> groupJoinByGroupIdAndMemberId = entityFacade.getGroupJoinByGroupIdAndMemberId(group.getId(), memberId);
        switch (groupJoinApplyReq.joinType()) {
            case JOIN -> {
                //중복 지원 체크
                groupJoinByGroupIdAndMemberId.ifPresent(val -> {
                    log.error("Value is present: " + val);
                    throw new GeneralHandler(ErrorStatus.GROUP_JOIN_CONFLICT);
                });

                /**
                 * 지원 완료
                 * PUBLIC: 즉시 그룹에 합류(maxPeople > currentPeople 일 때)
                 * PRIVATE: 지원 완료
                 */
                if (group.getGroupStatus().equals(GroupStatus.PUBLIC)) {
                    if (group.getMaxPeople() <= group.getCurrentPeople())
                        throw new GeneralHandler(ErrorStatus.GROUP_FULL);
                    joinGroup(group, member);
                } else {
                    GroupJoin groupJoin = new GroupJoin(null, group, member, groupJoinApplyReq.joinType());
                    groupJoinRepository.save(groupJoin);
                }
            }
            case CANCEL ->
                //지원 이력 체크
                groupJoinByGroupIdAndMemberId.ifPresentOrElse(
                        groupJoinRepository::delete,
                        () -> {
                            log.error("Value is not found");
                            throw new GeneralHandler(ErrorStatus.GROUP_JOIN_NOT_FOUND);
                        }
                );
        }
    }

    @Override
    public void decideJoin(Long memberId, GroupJoinDecideReq groupJoinDecideReq) {
        //지원 이력 삭제
        GroupJoin groupJoin = entityFacade.getGroupJoin(groupJoinDecideReq.groupJoinId());
        if (!groupJoin.getGroup().getGroupLeader().getId().equals(memberId))
            throw new GeneralHandler(ErrorStatus.USER_FORBIDDEN);
        groupJoinRepository.delete(groupJoin);
        joinGroup(groupJoin.getGroup(), groupJoin.getMember());
    }

    @Override
    public List<GroupJoinRes> getGroupJoinList(Long memberId, Long groupId) {
        List<GroupJoin> groupJoinsByGroupId = entityFacade.getGroupJoinsByGroupId(groupId);
        return groupJoinsByGroupId.stream()
                .map(GroupJoinRes::mapMemberToJoinRes)
                .toList();
    }

    @Override
    public List<GroupJoinMyRes> getMyGroupJoinList(Long memberId) {
        List<GroupJoin> groupJoinsByMemberId = entityFacade.getGroupJoinsByMemberId(memberId);
        return groupJoinsByMemberId.stream()
                .map(GroupJoinMyRes::mapGroupJoinToMyRes)
                .toList();
    }

    private void joinGroup(Group group, Member member) {
        GroupUser groupUser = new GroupUser(null, group, member);
        groupUserRepository.save(groupUser);
        group.increaseCurrentPeople();
    }
}
