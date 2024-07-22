package com.simbongsa.global;

import com.simbongsa.follows.entity.Follows;
import com.simbongsa.follows.repository.FollowsRepository;
import com.simbongsa.follows_requests.entity.FollowsRequests;
import com.simbongsa.follows_requests.repository.FollowsRequestRepository;
import com.simbongsa.global.common.apiPayload.code.statusEnums.ErrorStatus;
import com.simbongsa.global.common.exception.GeneralHandler;
import com.simbongsa.group.entity.Group;
import com.simbongsa.group.repository.GroupRepository;
import com.simbongsa.group_join.entity.GroupJoin;
import com.simbongsa.group_join.repository.GroupJoinRepository;
import com.simbongsa.group_user.entity.GroupUser;
import com.simbongsa.group_user.repository.GroupUserRepository;
import com.simbongsa.member.entity.Member;
import com.simbongsa.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntityFacade {
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final GroupJoinRepository groupJoinRepository;
    private final GroupUserRepository groupUserRepository;
    private final FollowsRepository followsRepository;
    private final FollowsRequestRepository followsRequestRepository;

    public Member getMember(Long memberId) {
        Optional<Member> memberById = memberRepository.findById(memberId);
        if (memberById.isEmpty())
            throw new GeneralHandler(ErrorStatus.USER_NOT_FOUND);
        return memberById.get();
    }

    public Group getGroup(Long groupId) {
        Optional<Group> groupById = groupRepository.findById(groupId);
        if (groupById.isEmpty())
            throw new GeneralHandler(ErrorStatus.GROUP_NOT_FOUND);
        return groupById.get();
    }

    public GroupJoin getGroupJoin(Long groupJoinId) {
        Optional<GroupJoin> groupJoinById = groupJoinRepository.findById(groupJoinId);
        if (groupJoinById.isEmpty())
            throw new GeneralHandler(ErrorStatus.GROUP_JOIN_NOT_FOUND);
        return groupJoinById.get();
    }

    public Optional<Follows> getFollowsByFollowingMemberAndFollowedMember(Member followingMember, Member followedMember) {
        return followsRepository.findByFollowingMemberAndFollowedMember(followingMember, followedMember);
    }

    public List<GroupJoin> getGroupJoinsByMemberId(Long memberId) {
        return groupJoinRepository.findByMember_Id(memberId);
    }

    public List<GroupJoin> getGroupJoinsByGroupId(Long groupId) {
        return groupJoinRepository.findByGroup_Id(groupId);
    }
    public Optional<GroupJoin> getGroupJoinByGroupIdAndMemberId(Long groupId, Long memberId) {
        return groupJoinRepository.findByGroup_IdAndMember_Id(groupId, memberId);
    }

    public List<GroupUser> getGroupUserByGroupId(Long groupId) {
        return groupUserRepository.findByGroup_Id(groupId);
    }

    public GroupUser getGroupUserByGroupIdAndMemberId(Long groupId, Long memberId) {
        Optional<GroupUser> groupUserByGroupIdAndMemberId = groupUserRepository.findByGroup_IdAndMember_Id(groupId, memberId);
        if (groupUserByGroupIdAndMemberId.isEmpty())
            throw new GeneralHandler(ErrorStatus.GROUP_USER_NOT_FOUND);
        return groupUserByGroupIdAndMemberId.get();
    }

    public FollowsRequests getFollowsRequests(Long followsRequestsId) {
        return followsRequestRepository.findById(followsRequestsId).orElseThrow(
                () -> new GeneralHandler(ErrorStatus.FOLLOWS_REQUESTS_NOT_FOUND));
    }

    public Optional<Follows> getFollowsByFollowingMemberAndFollowedMember(Member followingMember, Member followedMember) {
        return followsRepository.findByFollowingMemberAndFollowedMember(followingMember, followedMember);
    }

    public List<FollowsRequests> getFollowsRequestsListByFollowedMemberId(Long memberId) {
        return followsRequestRepository.findAllByFollowedMemberId(memberId);
    }

    public List<FollowsRequests> getFollowsRequestsListByFollowingMemberId(Long memberId) {
        return followsRequestRepository.findAllByFollowingMemberId(memberId);
    }
}
