package com.simbongsa.feed.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simbongsa.feed.entity.Feed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import static com.simbongsa.feed.entity.QFeed.feed;
@RequiredArgsConstructor
@Repository
public class FeedRepositoryCustomImpl implements FeedRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    @Override
    public Slice<Feed> getFeeds(Long memberId, Long lastFeedId, Pageable pageable) {
        List<Feed> feedList = getFeedList(memberId, lastFeedId, pageable);
        return checkLastPage(pageable, feedList);
    }

    List<Feed> getFeedList(Long memberId, Long lastFeedId, Pageable pageable) {
        return queryFactory
                .select(feed)
                .from(feed)
                .where(
                        ltFeedId(lastFeedId),
                        eqMemberId(memberId)
                )
                .orderBy(feed.createdAt.desc())
                .limit(pageable.getPageSize()+1)
                .fetch();
    }

    private BooleanExpression ltFeedId(Long feedId) {
        if (feedId == null) return null;
        return feed.id.lt(feedId);
    }

    private BooleanExpression eqMemberId(Long memberId) {
        return feed.member.id.eq(memberId);
    }

    private Slice<Feed> checkLastPage(Pageable pageable, List<Feed> results) {

        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }
}
