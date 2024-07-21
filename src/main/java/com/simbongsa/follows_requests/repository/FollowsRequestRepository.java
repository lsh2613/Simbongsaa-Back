package com.simbongsa.follows_requests.repository;

import com.simbongsa.follows_requests.entity.FollowsRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowsRequestRepository extends JpaRepository<FollowsRequests, Long> {
    List<FollowsRequests> findAllByFollowedMemberId(Long followedMemberId);
    List<FollowsRequests> findAllByFollowingMemberId(Long followedMemberId);
}
