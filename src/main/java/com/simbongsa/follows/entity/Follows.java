package com.simbongsa.follows.entity;

import com.simbongsa.common.entity.BaseEntity;
import com.simbongsa.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "FOLLOWS")
public class Follows extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follows_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member followingMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member followedMemberId;
}
