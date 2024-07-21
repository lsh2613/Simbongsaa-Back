package com.simbongsa.follows_requests.service;

import com.simbongsa.follows.entity.Follows;
import com.simbongsa.follows.repository.FollowsRepository;
import com.simbongsa.follows_requests.dto.req.FollowsRequestsDecideReq;
import com.simbongsa.follows_requests.dto.res.FollowsRequestsRes;
import com.simbongsa.follows_requests.entity.FollowsRequests;
import com.simbongsa.follows_requests.repository.FollowsRequestRepository;
import com.simbongsa.global.EntityFacade;
import com.simbongsa.global.common.apiPayload.code.statusEnums.ErrorStatus;
import com.simbongsa.global.common.constant.FollowsRequestsDecide;
import com.simbongsa.global.common.constant.MemberStatus;
import com.simbongsa.global.common.exception.GeneralHandler;
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
public class FollowsRequestsServiceImpl implements FollowsRequestsService{

    private final FollowsRepository followsRepository;
    private final FollowsRequestRepository followsRequestRepository;
    private final EntityFacade entityFacade;

    @Override
    public void follow(Long followingId, Long followedMemberId) {
        Member followingMember = entityFacade.getMember(followingId);
        Member followedMember = entityFacade.getMember(followedMemberId);

        Optional<Follows> optionalFollows = entityFacade.getFollowsByFollowingMemberAndFollowedMember(followingMember, followedMember);

        /**
         * follows 존재?
         *  - YES
         *     - PUBLIC, PRIVATE -> follows 삭제 (=팔로우 취소)
         *  - NO
         *     - PUBLIC -> follows 저장 (=팔로우)
         *     - PRIVATE -> follows_requests 저장 (=팔로우 요청)
         */
        optionalFollows.ifPresentOrElse(
                followsRepository::delete,
                () -> followByFollowedMemberStatus(followingMember, followedMember, followedMember.getMemberStatus())
        );
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
    public void decideRequests(Long loginId, FollowsRequestsDecideReq followsRequestsDecideReq) {
        FollowsRequests followsRequests = entityFacade.getFollowsRequests(followsRequestsDecideReq.followsRequestsId());
        Member followedMember = followsRequests.getFollowedMember();

        deleteFollowsRequests(loginId, followedMember.getId(), followsRequests);

        FollowsRequestsDecide decideType = followsRequestsDecideReq.followRequestsDecide();
        followsIfAccept(decideType, followedMember, followsRequests);
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
    public List<FollowsRequestsRes> getFollowsRequestsList(Long memberId) {
        List<FollowsRequests> requests = entityFacade.getFollowsRequestsListByFollowedMemberId(memberId);
        return requests.stream()
                .map(FollowsRequestsRes::mapFollowingMemberToRequestsRes)
                .toList();
    }

    @Override
    public List<FollowsRequestsRes> getMyFollowsRequestsList(Long memberId) {
        List<FollowsRequests> requests = entityFacade.getFollowsRequestsListByFollowingMemberId(memberId);
        return requests.stream()
                .map(FollowsRequestsRes::mapFollowedMemberToRequestsRes)
                .toList();
    }
}
