package com.simbongsa.follows_requests.service;

import com.simbongsa.follows_requests.dto.req.FollowsRequestsDecideReq;
import com.simbongsa.follows_requests.dto.res.FollowsRequestsRes;

import java.util.List;

public interface FollowsRequestsService {
    void follow(Long followingId, Long followedMemberId);

    void decideRequests(Long loginId, FollowsRequestsDecideReq followsRequestsDecideReq);

    List<FollowsRequestsRes> getReceivedFollowsRequestsList(Long memberId);

    List<FollowsRequestsRes> getSentFollowsRequestsList(Long memberId);

    void delete(Long loginId, Long followsRequestsId);
}
