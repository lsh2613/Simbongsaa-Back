package com.simbongsa.follows.service;

import com.simbongsa.follows.dto.res.FollowsPageRes;
import org.springframework.data.domain.Pageable;

public interface FollowsService {
    void deleteMyFollows(Long loginId, Long followsId);

    void deleteOppositeFollows(Long loginId, Long followsId);

    FollowsPageRes getMyFollowingPage(Long loginId, Long lastFollowsId, Pageable pageable);

    FollowsPageRes getMemberFollowingPage(Long loginId, Long memberId, Long lastFollowsId, Pageable pageable);

    FollowsPageRes getMyFollowerPage(Long loginId, Long lastFollowsId, Pageable pageable);

    FollowsPageRes getMemberFollowerPage(Long loginId, Long memberId, Long lastFollowsId, Pageable pageable);
}
