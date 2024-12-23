package com.simbongsa.member.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.simbongsa.global.EntityFacade;
import com.simbongsa.global.common.apiPayload.code.statusEnums.ErrorStatus;
import com.simbongsa.global.common.exception.GeneralHandler;
import com.simbongsa.global.jwt.service.TokenProvider;
import com.simbongsa.global.redis.util.RedisUtil;
import com.simbongsa.member.entity.Member;
import com.simbongsa.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

import static com.simbongsa.global.common.apiPayload.code.statusEnums.ErrorStatus.TOKEN_ID_MISMATCH;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final EntityFacade entityFacade;
    private final TokenProvider tokenProvider;
    private final RedisUtil redisUtil;

    @Override
    public void deleteMember(Long loginMemberId) {
        Member member = entityFacade.getMember(loginMemberId);
        memberRepository.delete(member);
    }

    @Override
    public void logout(String accessToken, String refreshToken, Long memberId) {
        DecodedJWT decodedAccessToken = tokenProvider.decodedJWT(accessToken);
        Long accessTokenId = decodedAccessToken.getClaim("id").asLong();

        DecodedJWT decodedRefreshToken = tokenProvider.decodedJWT(refreshToken);
        Long refreshTokenId = decodedRefreshToken.getClaim("id").asLong();

        if (accessTokenId != memberId || refreshTokenId != memberId) {
            throw new GeneralHandler(TOKEN_ID_MISMATCH);
        }

        Date expiresAt = decodedAccessToken.getExpiresAt();
        long diff = expiresAt.getTime() - System.currentTimeMillis();
        redisUtil.setBlackList(accessToken, accessTokenId, diff);

        redisUtil.deleteData(refreshToken);
    }
}
