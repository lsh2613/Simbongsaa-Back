package com.simbongsa.group.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simbongsa.group.dto.req.GroupSearchReq;
import com.simbongsa.group.entity.Group;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.simbongsa.group.entity.QGroup.group;

@RequiredArgsConstructor
@Repository
public class GroupRepositoryCustomImpl implements GroupRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Group> searchGroup(Pageable pageable, GroupSearchReq groupSearchReq) {
        List<Group> groupList = getGroupList(pageable, groupSearchReq);
        Long groupCount = getGroupCount(groupSearchReq);
        return new PageImpl<>(groupList, pageable, groupCount);
    }

    private List<Group> getGroupList(Pageable pageable, GroupSearchReq groupSearchReq) {
        return queryFactory
                .select(group)
                .from(group)
                .where(titleLike(groupSearchReq.keyword()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(group.createdAt.desc())
                .fetch();
    }

    private Long getGroupCount(GroupSearchReq groupSearchReq) {
        return queryFactory
                .select(group.count())
                .from(group)
                .where(titleLike(groupSearchReq.keyword()))
                .fetchOne();
    }

    private BooleanExpression titleLike(String keyword) {
        return StringUtils.hasText(keyword) ? group.name.contains(keyword) : null;
    }
}
