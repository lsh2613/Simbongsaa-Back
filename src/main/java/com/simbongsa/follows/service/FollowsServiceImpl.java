package com.simbongsa.follows.service;

import com.simbongsa.follows.dto.res.FollowsPageRes;
import com.simbongsa.follows.entity.Follows;
import com.simbongsa.follows.repository.FollowsRepository;
import com.simbongsa.follows.repository.FollowsRepositoryCustom;
import com.simbongsa.global.EntityFacade;
import com.simbongsa.global.common.apiPayload.code.statusEnums.ErrorStatus;
import com.simbongsa.global.common.constant.MemberStatus;
import com.simbongsa.global.common.exception.GeneralHandler;
import com.simbongsa.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class FollowsServiceImpl implements FollowsService {
    private final FollowsRepository followsRepository;
    private final FollowsRepositoryCustom followsRepositoryCustom;
    private final EntityFacade entityFacade;

    @Override
    public void deleteMyFollows(Long loginId, Long followsId) {
        Follows follows = entityFacade.getFollows(followsId);
        validateFollowsTarget(loginId, follows.getFollowingMember().getId());
        followsRepository.delete(follows);
    }

    @Override
    public void deleteOppositeFollows(Long loginId, Long followsId) {
        Follows follows = entityFacade.getFollows(followsId);
        validateFollowsTarget(loginId, follows.getFollowedMember().getId());
        followsRepository.delete(follows);
    }

    @Override
    public FollowsPageRes getMyFollowingPage(Long loginId, Long lastFollowsId, Pageable pageable) {
        Slice<Follows> myFollowingPage = followsRepositoryCustom.getFollowingPage(loginId, lastFollowsId, pageable);
        return FollowsPageRes.mapFollowingPageToPageRes(myFollowingPage);
    }

    @Override
    public FollowsPageRes getMemberFollowingPage(Long loginId, Long memberId, Long lastFollowsId, Pageable pageable) {
        // member의 상태가 PRIVATE일 경우 login->member를 팔로우하고 있어야 조회 가능
        Member targetMember = entityFacade.getMember(memberId);

        if (targetMember.getMemberStatus() == MemberStatus.PRIVATE) {
            verifyFollowingRelationship(loginId, targetMember);
        }

        Slice<Follows> memberFollowingPage = followsRepositoryCustom.getFollowingPage(memberId, lastFollowsId, pageable);
        return FollowsPageRes.mapFollowingPageToPageRes(memberFollowingPage);
    }

    @Override
    public FollowsPageRes getMyFollowerPage(Long loginId, Long lastFollowsId, Pageable pageable) {
        Slice<Follows> myFollowerPage = followsRepositoryCustom.getFollowerPage(loginId, lastFollowsId, pageable);
        return FollowsPageRes.mapFollowerPageToPageRes(myFollowerPage);
    }

    @Override
    public FollowsPageRes getMemberFollowerPage(Long loginId, Long memberId, Long lastFollowsId, Pageable pageable) {
        // member의 상태가 PRIVATE일 경우 login->member를 팔로우하고 있어야 조회 가능
        Member targetMember = entityFacade.getMember(memberId);

        if (targetMember.getMemberStatus() == MemberStatus.PRIVATE) {
            verifyFollowingRelationship(loginId, targetMember);
        }

        Slice<Follows> memberFollowerPage = followsRepositoryCustom.getFollowerPage(memberId, lastFollowsId, pageable);
        return FollowsPageRes.mapFollowerPageToPageRes(memberFollowerPage);
    }

    private void verifyFollowingRelationship(Long loginId, Member targetMember) {
        Member loginMember = entityFacade.getMember(loginId);
        Optional<Follows> optionalFollows = entityFacade.getFollowsByFollowingMemberAndFollowedMember(loginMember, targetMember);
        if (!optionalFollows.isPresent()) {
            throw new GeneralHandler(ErrorStatus.USER_FORBIDDEN);
        }
    }

    private void validateFollowsTarget(Long loginId, Long targetId) {
        if (!loginId.equals(targetId)) {
            throw new GeneralHandler(ErrorStatus.USER_FORBIDDEN);
        }
    }

}
