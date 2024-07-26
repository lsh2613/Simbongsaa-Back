package com.simbongsa.member.entity;


import com.simbongsa.feed.entity.Feed;
import com.simbongsa.global.common.constant.MemberStatus;
import com.simbongsa.global.common.entity.BaseEntity;
import com.simbongsa.global.common.constant.OauthProvider;
import com.simbongsa.global.common.constant.Role;
import com.simbongsa.global.oauth2.member.OauthMember;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MEMBER")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String socialId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OauthProvider oauthProvider;

    private String email;

    // 중복 여부 체크 필요!
    private String nickname;

    private Integer age;

    private String profileImg;

    private Role role;

    private String introduction;

    private Integer volunteerParticipationCnt;

    private MemberStatus memberStatus;
  
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feed> feeds = new ArrayList<>();

    public Member(OauthMember request) {
        this.socialId = request.getSocialId();
        this.oauthProvider = request.getOauthProvider();
        this.email = request.getEmail();
        this.nickname = request.getNickname();
        this.role = Role.GUEST;
    }

    public Member(String socialId, OauthProvider oauthProvider, String email, String nickname, int age,
                      String profileImg, Role role, String introduction, int volunteerParticipationCnt, MemberStatus memberStatus) {
        this.socialId = socialId;
        this.oauthProvider = oauthProvider;
        this.email = email;
        this.nickname = nickname;
        this.age = age;
        this.profileImg = profileImg;
        this.role = role;
        this.introduction = introduction;
        this.volunteerParticipationCnt = volunteerParticipationCnt;
        this.memberStatus = memberStatus;
    }
  
    public void addFeed(Feed feed) {
        if (feeds == null) feeds = new ArrayList<>();
        feeds.add(feed);
        feed.setMember(this);  // Feed 객체와의 양방향 연관관계 설정
    }
}
