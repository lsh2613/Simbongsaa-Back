package com.simbongsa.follows_requests.entity;

import com.simbongsa.global.common.entity.BaseEntity;
import com.simbongsa.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "FOLLOWS_REQUESTS")
public class FollowsRequests extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follows_requests_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_member_id")
    private Member followingMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed_member_id")
    private Member followedMember;

    public FollowsRequests(Member loginMember, Member member) {
        this.followingMember = loginMember;
        this.followedMember = member;
    }
}
