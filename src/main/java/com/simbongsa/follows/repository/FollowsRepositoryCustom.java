package com.simbongsa.follows.repository;

import com.simbongsa.follows.entity.Follows;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface FollowsRepositoryCustom {
    Slice<Follows> getFollowingPage(Long loginId, Long lastFollowsId, Pageable pageable);
    Slice<Follows> getFollowerPage(Long loginId, Long lastFollowsId, Pageable pageable);
}
