package com.simbongsa.follows_requests.repository;

import com.simbongsa.follows_requests.entity.FollowsRequests;
import com.simbongsa.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowsRequestRepository extends JpaRepository<FollowsRequests, Long> {
    Optional<FollowsRequests> findByFollowingMemberAndFollowedMember(Member followingMember, Member followedMember);
}
