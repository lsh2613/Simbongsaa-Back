package com.simbongsa.follows_requests.service;

import com.simbongsa.follows_requests.dto.req.FollowsRequestsDecideReq;

public interface FollowsRequestsService {
    void follow(Long followingId, Long followedMemberId);

    void decideRequests(Long loginId, FollowsRequestsDecideReq followsRequestsDecideReq);

}
