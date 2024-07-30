package com.simbongsa.follows.service;

public interface FollowsService {
    void deleteMyFollows(Long loginId, Long followsId);

    void deleteOppositeFollows(Long loginId, Long followsId);
}
