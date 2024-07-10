package com.simbongsa.member.entity;

import com.simbongsa.global.common.entity.BaseEntity;
import com.simbongsa.global.common.constant.OauthProvider;
import com.simbongsa.global.common.constant.Role;
import com.simbongsa.global.oauth2.member.OauthMember;
import jakarta.persistence.*;
import lombok.*;

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

    public Member(OauthMember request) {
        this.socialId = request.getSocialId();
        this.oauthProvider = request.getOauthProvider();
        this.email = request.getEmail();
        this.nickname = request.getNickname();
        this.role = Role.GUEST;
    }
}
