package com.simbongsa.global.oauth2.service;

import com.simbongsa.global.common.constant.Role;
import com.simbongsa.global.oauth2.dto.res.OauthLoginRes;
import com.simbongsa.global.oauth2.member.OauthMember;
import com.simbongsa.global.oauth2.param.OauthParams;
import com.simbongsa.member.entity.Member;
import com.simbongsa.global.jwt.service.TokenProvider;
import com.simbongsa.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class OauthServiceImpl implements OauthService{
    /**
     * Service
     */
    private final RequestOauthInfoService requestOauthInfoService;
    private final TokenProvider tokenProvider;

    /**
     * repository
     */
    private final MemberRepository memberRepository;

    @Override
    public OauthLoginRes getMemberByOauthLogin(OauthParams oauthParams) {
        log.debug("------- Oauth 로그인 시도 -------");

        OauthMember request = requestOauthInfoService.request(oauthParams);
        Optional<Member> byOauthProviderAndSocialId = memberRepository.findByOauthProviderAndSocialId(request.getOauthProvider(), request.getSocialId());


        if (byOauthProviderAndSocialId.isPresent()) {
            //기존 유저
            if (byOauthProviderAndSocialId.get().getRole().equals(Role.USER))
                return new OauthLoginRes(
                        byOauthProviderAndSocialId.get().getId(),
                        byOauthProviderAndSocialId.get().getRole(),
                        tokenProvider.issueAccessToken(byOauthProviderAndSocialId.get().getId()),
                        tokenProvider.issueRefreshToken(byOauthProviderAndSocialId.get().getId()));
            //신규 게스트
            else memberRepository.deleteById(byOauthProviderAndSocialId.get().getId());
        }

        //신규 유저 DB에 저장
        Member savedMember = memberRepository.save(new Member(request));
        return new OauthLoginRes(savedMember.getId(), savedMember.getRole(), null,null);
    }
}
