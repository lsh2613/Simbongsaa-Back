package com.simbongsa.follows_requests.service;

import com.simbongsa.follows.entity.Follows;
import com.simbongsa.follows.repository.FollowsRepository;
import com.simbongsa.follows_requests.entity.FollowsRequests;
import com.simbongsa.follows_requests.repository.FollowsRequestRepository;
import com.simbongsa.global.EntityFacade;
import com.simbongsa.global.common.constant.MemberStatus;
import com.simbongsa.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

}
