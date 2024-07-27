package com.simbongsa.follows_requests.service;

import com.simbongsa.follows.entity.Follows;
import com.simbongsa.follows.repository.FollowsRepository;
import com.simbongsa.follows_requests.dto.res.FollowsRequestsPageRes;
import com.simbongsa.follows_requests.entity.FollowsRequests;
import com.simbongsa.follows_requests.repository.FollowsRequestRepository;
import com.simbongsa.follows_requests.repository.FollowsRequestsRepositoryCustom;
import com.simbongsa.global.EntityFacade;
import com.simbongsa.global.common.apiPayload.code.statusEnums.ErrorStatus;
import com.simbongsa.global.common.constant.FollowsRequestsDecide;
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
public class FollowsRequestsServiceImpl implements FollowsRequestsService{

    private final FollowsRepository followsRepository;
    private final FollowsRequestRepository followsRequestRepository;
    private final FollowsRequestsRepositoryCustom followsRequestsRepositoryCustom;
    private final EntityFacade entityFacade;

    @Override
    public void follow(Long followingId, Long followedMemberId) {
        Member followingMember = entityFacade.getMember(followingId);
        Member followedMember = entityFacade.getMember(followedMemberId);

        Optional<Follows> optionalFollows = entityFacade.getFollowsByFollowingMemberAndFollowedMember(followingMember, followedMember);
        Optional<FollowsRequests> optionalFollowsRequests = entityFacade.getFollowsRequestsByFollowingMemberAndFollowedMember(followingMember, followedMember);

        /** todo
         *  Follows.follows()
         *  - PUBLIC -> follows 저장 (=팔로우)
         *  - PRIVATE -> follows_requests 저장 (=팔로우 요청)
         *  Follows.unFollows()
         *  - 팔로우 삭제 (언팔)
         *  FollowsRequests.delete()
         *  - 팔로우 요청 삭제
         */
        if (optionalFollows.isPresent()) {
            throw new GeneralHandler(ErrorStatus.DUPLICATED_FOLLOWS);
        }
        if (optionalFollowsRequests.isPresent()) {
            throw new GeneralHandler(ErrorStatus.DUPLICATED_FOLLOWS);
        }

        followByFollowedMemberStatus(followingMember, followedMember, followedMember.getMemberStatus());
    }

    private void followByFollowedMemberStatus(Member followingMember, Member followedMember, MemberStatus memberStatus) {
        switch (memberStatus){
            case PUBLIC:
                followsRepository.save(new Follows(followingMember, followedMember));
                break;
            case PRIVATE:
                followsRequestRepository.save(new FollowsRequests(followingMember, followedMember));
        }
    }

    @Override
    public void decideRequests(Long loginId, Long followsRequestsId, FollowsRequestsDecide followRequestsDecide) {
        FollowsRequests followsRequests = entityFacade.getFollowsRequests(followsRequestsId);
        Member followedMember = followsRequests.getFollowedMember();

        deleteFollowsRequests(loginId, followedMember.getId(), followsRequests);

        followsIfAccept(followRequestsDecide, followedMember, followsRequests);
    }

    private void followsIfAccept(FollowsRequestsDecide followRequestsDecide, Member followedMember, FollowsRequests followsRequests) {
        if (followRequestsDecide == FollowsRequestsDecide.ACCEPT) {
            Follows follows = new Follows(followsRequests.getFollowingMember(), followedMember);
            followsRepository.save(follows);
        }
    }

    private void deleteFollowsRequests(Long loginId, Long followedMemberId, FollowsRequests followsRequests) {
        if (!validateFollowsRequestsTarget(loginId, followedMemberId)) {
            throw new GeneralHandler(ErrorStatus.USER_FORBIDDEN);
        }
        followsRequestRepository.delete(followsRequests);
    }

    private static boolean validateFollowsRequestsTarget(Long loginId, Long followedMemberId) {
        return followedMemberId.equals(loginId);
    }

    @Override
    public FollowsRequestsPageRes getReceivedFollowsRequestsPage(Long memberId, Long lastFollowsRequestId, Pageable pageable) {
        Slice<FollowsRequests> receivedFollowsRequestsPage = followsRequestsRepositoryCustom.getReceivedFollowsRequestsPage(memberId, lastFollowsRequestId, pageable);
        return FollowsRequestsPageRes.mapReceivedRequestsToPageRes(receivedFollowsRequestsPage);
    }

    @Override
    public FollowsRequestsPageRes getSentFollowsRequestsPage(Long memberId, Long lastFollowsRequestId, Pageable pageable) {
        Slice<FollowsRequests> sentFollowsRequestsPage = followsRequestsRepositoryCustom.getSentFollowsRequestsPage(memberId, lastFollowsRequestId, pageable);
        return FollowsRequestsPageRes.mapSentRequestsToPageRes(sentFollowsRequestsPage);
    }

    @Override
    public void delete(Long loginId, Long followsRequestsId) {
        Member member = entityFacade.getMember(loginId);
        FollowsRequests followsRequests = entityFacade.getFollowsRequests(followsRequestsId);

        if (!followsRequests.getFollowingMember().equals(member)) {
            throw new GeneralHandler(ErrorStatus.USER_FORBIDDEN);
        }

        followsRequestRepository.delete(followsRequests);
    }
}
