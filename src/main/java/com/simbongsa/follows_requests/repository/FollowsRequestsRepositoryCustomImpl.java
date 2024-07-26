package com.simbongsa.follows_requests.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simbongsa.follows_requests.entity.FollowsRequests;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.simbongsa.follows_requests.entity.QFollowsRequests.followsRequests;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FollowsRequestsRepositoryCustomImpl implements FollowsRequestsRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<FollowsRequests> getSentFollowsRequestsPage(Long memberId, Long lastFollowsRequestsId, Pageable pageable) {
        List<FollowsRequests> followsRequestsList = getSentFollowsRequestsList(memberId, lastFollowsRequestsId, pageable);
        Boolean hasNext = hasNextPage(followsRequestsList, pageable);
        return new SliceImpl<>(followsRequestsList, pageable, hasNext);
    }

    private List<FollowsRequests> getSentFollowsRequestsList(Long memberId, Long lastFollowsRequestsId, Pageable pageable) {
        return queryFactory
                .selectFrom(followsRequests)
                .where(
                        ltFollowsRequestsId(lastFollowsRequestsId),
                        eqFollowingMemberId(memberId)
                )
                .orderBy(followsRequests.createdAt.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();
    }

    @Override
    public Slice<FollowsRequests> getReceivedFollowsRequestsPage(Long memberId, Long lastFollowsRequestsId, Pageable pageable) {
        List<FollowsRequests> followsRequestsList = getReceivedFollowsRequestsList(memberId, lastFollowsRequestsId, pageable);
        Boolean hasNext = hasNextPage(followsRequestsList, pageable);
        return new SliceImpl<>(followsRequestsList, pageable, hasNext);
    }

    private List<FollowsRequests> getReceivedFollowsRequestsList(Long memberId, Long lastFollowsRequestsId, Pageable pageable) {
        return queryFactory
                .selectFrom(followsRequests)
                .where(
                        ltFollowsRequestsId(lastFollowsRequestsId),
                        eqFollowedMemberId(memberId)
                )
                .orderBy(followsRequests.createdAt.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();
    }

    private BooleanExpression ltFollowsRequestsId(Long followsRequestsId) {
        if (followsRequestsId == null) return null;
        return followsRequests.id.lt(followsRequestsId);
    }

    private BooleanExpression eqFollowingMemberId(Long memberId) {
        return followsRequests.followingMember.id.eq(memberId);
    }

    private BooleanExpression eqFollowedMemberId(Long memberId) {
        return followsRequests.followedMember.id.eq(memberId);
    }

    private Boolean hasNextPage(List<FollowsRequests> followsRequestsList, Pageable pageable) {
        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (followsRequestsList.size() > pageable.getPageSize()) {
            followsRequestsList.remove(pageable.getPageSize());
            return true;
        }
        return false;
    }

}
