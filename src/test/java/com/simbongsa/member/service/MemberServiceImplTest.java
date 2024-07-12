package com.simbongsa.member.service;

import com.simbongsa.global.EntityFacade;
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

    Member createMember(Long id) {
        Member member = new Member(
                id,
                "3231321",
                OauthProvider.GOOGLE,
                "test@gmail.com",
                "새싹개발자",
                22,
                null,
                Role.USER,
                "안녕하세요 개발자입니다.",
                0
        );
        return member;
    }

    @Test
    void deleteMember() {
        //given
        Member member = createMember(1L);
        memberRepository.save(member);

        //when
        memberService.deleteMember(1L);

        //then
        assertThrows(GeneralHandler.class, () -> entityFacade.getMember(1L));
    }

    @Test
    void 로그아웃_성공() {
        //given
        Member member = createMember(1L);
        memberRepository.save(member);

        String accessToken = tokenProvider.issueAccessToken(1L);
        String refreshToken = tokenProvider.issueRefreshToken(1L);

        //when
        memberService.logout(accessToken, refreshToken, 1L);

        //then
        assertThat(redisUtil.getData(refreshToken)).isNull();
        assertThat(redisUtil.containsInBlackList(accessToken)).isTrue();
    }

    @Test
    void 블랙리스트_검증_성공() {
        //given
        Member member = createMember(1L);
        memberRepository.save(member);

        String accessToken = tokenProvider.issueAccessToken(1L);
        String refreshToken = tokenProvider.issueRefreshToken(1L);

        //when
        memberService.logout(accessToken, refreshToken, 1L);

        //then
        assertThat(redisUtil.getData(refreshToken)).isNull();
        assertThrows(GeneralHandler.class, () -> redisUtil.checkTokenBlacklisted(accessToken));
    }
}
