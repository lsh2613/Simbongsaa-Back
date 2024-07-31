package com.simbongsa.follows.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simbongsa.follows.entity.Follows;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.simbongsa.follows.entity.QFollows.follows;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FollowsRepositoryCustomImpl implements FollowsRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Follows> getFollowingPage(Long memberId, Long lastFollowsId, Pageable pageable) {
        List<Follows> followingList = getFollowingList(memberId, lastFollowsId, pageable);
        boolean hasNext = hasNextPage(followingList, pageable);
        return new SliceImpl<>(followingList, pageable, hasNext);
    }

    @Override
    public Slice<Follows> getFollowerPage(Long memberId, Long lastFollowsId, Pageable pageable) {
        List<Follows> followingList = getFollowerList(memberId, lastFollowsId, pageable);
        boolean hasNext = hasNextPage(followingList, pageable);
        return new SliceImpl<>(followingList, pageable, hasNext);
    }

    private List<Follows> getFollowerList(Long memberId, Long lastFollowsId, Pageable pageable) {
        return queryFactory
                .selectFrom(follows)
                .where(
                        ltFollowsId(lastFollowsId),
                        eqFollowedMemberId(memberId)
                )
                .orderBy(follows.createdAt.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();
    }

    private BooleanExpression eqFollowedMemberId(Long memberId) {
        return follows.followedMember.id.eq(memberId);
    }

    private List<Follows> getFollowingList(Long memberId, Long lastFollowsId, Pageable pageable) {
        return queryFactory
                .selectFrom(follows)
                .where(
                        ltFollowsId(lastFollowsId),
                        eqFollowingMemberId(memberId)
                )
                .orderBy(follows.createdAt.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();
    }

    private BooleanExpression eqFollowingMemberId(Long memberId) {
        return follows.followingMember.id.eq(memberId);
    }

    private BooleanExpression ltFollowsId(Long followsId) {
        if (followsId == null) return null;
        return follows.id.lt(followsId);
    }

    private Boolean hasNextPage(List<Follows> followsList, Pageable pageable) {
        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (followsList.size() > pageable.getPageSize()) {
            followsList.remove(pageable.getPageSize());
            return true;
        }
        return false;
    }
}
