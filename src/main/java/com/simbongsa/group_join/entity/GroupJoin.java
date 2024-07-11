package com.simbongsa.group_join.entity;

import com.simbongsa.global.common.entity.BaseEntity;
import com.simbongsa.group.entity.Group;
import com.simbongsa.group_join.JoinType;
import com.simbongsa.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "GROUP_JOIN")
public class GroupJoin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_join_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JoinType joinType;

}
