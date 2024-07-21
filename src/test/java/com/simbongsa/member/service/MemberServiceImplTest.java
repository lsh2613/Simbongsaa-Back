package com.simbongsa.member.service;

import com.simbongsa.global.EntityFacade;
import com.simbongsa.global.common.constant.MemberStatus;
import com.simbongsa.global.common.constant.OauthProvider;
import com.simbongsa.global.common.constant.Role;
import com.simbongsa.global.common.exception.GeneralHandler;
import com.simbongsa.global.jwt.service.TokenProvider;
import com.simbongsa.global.redis.util.RedisUtil;
import com.simbongsa.member.entity.Member;
import com.simbongsa.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberServiceImplTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private EntityFacade entityFacade;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private RedisUtil redisUtil;

    Member createMember() {
        Member member = new Member(
                "3231321",
                OauthProvider.GOOGLE,
                "test@gmail.com",
                "새싹개발자",
                22,
                null,
                Role.USER,
                "안녕하세요 개발자입니다.",
                0,
                MemberStatus.PUBLIC
        );
        return member;
    }

    @Test
    void deleteMember() {
        //given
        Member member = createMember();
        Member saveMember = memberRepository.save(member);

        //when
        memberService.deleteMember(saveMember.getId());

        //then
        assertThrows(GeneralHandler.class, () -> entityFacade.getMember(saveMember.getId()));
    }

    @Test
    void 로그아웃_성공() {
        //given
        Member member = createMember();
        Member saveMember = memberRepository.save(member);

        String accessToken = tokenProvider.issueAccessToken(saveMember.getId());
        String refreshToken = tokenProvider.issueRefreshToken(saveMember.getId());

        //when
        memberService.logout(accessToken, refreshToken, saveMember.getId());

        //then
        assertThat(redisUtil.getData(refreshToken)).isNull();
        assertThat(redisUtil.containsInBlackList(accessToken)).isTrue();
    }

    @Test
    void 블랙리스트_검증_성공() {
        //given
        Member member = createMember();
        Member saveMember = memberRepository.save(member);

        String accessToken = tokenProvider.issueAccessToken(saveMember.getId());
        String refreshToken = tokenProvider.issueRefreshToken(saveMember.getId());

        //when
        memberService.logout(accessToken, refreshToken, saveMember.getId());

        //then
        assertThat(redisUtil.getData(refreshToken)).isNull();
        assertThrows(GeneralHandler.class, () -> redisUtil.checkTokenBlacklisted(accessToken));
    }
}
