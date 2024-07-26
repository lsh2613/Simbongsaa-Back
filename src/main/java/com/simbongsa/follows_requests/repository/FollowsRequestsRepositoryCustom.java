package com.simbongsa.follows_requests.repository;

import com.simbongsa.follows_requests.entity.FollowsRequests;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface FollowsRequestsRepositoryCustom {
    Slice<FollowsRequests> getSentFollowsRequestsPage(Long memberId, Long lastFollowsRequestsId, Pageable pageable);
    Slice<FollowsRequests> getReceivedFollowsRequestsPage(Long memberId, Long lastFollowsRequestsId, Pageable pageable);
}
