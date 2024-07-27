package com.simbongsa.follows_requests.service;

import com.simbongsa.follows_requests.dto.res.FollowsRequestsPageRes;
import com.simbongsa.global.common.constant.FollowsRequestsDecide;
import org.springframework.data.domain.Pageable;


public interface FollowsRequestsService {
    void follow(Long followingId, Long followedMemberId);

    void decideRequests(Long loginId, Long followsRequestsId, FollowsRequestsDecide followRequestsDecide);

    void delete(Long loginId, Long followsRequestsId);

    FollowsRequestsPageRes getSentFollowsRequestsPage(Long memberId, Long lastFollowsRequestId, Pageable pageable);

    FollowsRequestsPageRes getReceivedFollowsRequestsPage(Long memberId, Long lastFollowsRequestId, Pageable pageable);
}
