package com.simbongsa.member.controller;

import com.simbongsa.global.common.apiPayload.CustomApiResponse;
import com.simbongsa.global.common.constant.TokenType;
import com.simbongsa.global.jwt.service.TokenProvider;
import com.simbongsa.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    /**
     * 로그아웃
     * @param request
     * @param memberId 회원id
     * @return null
     */
    @PostMapping("/logout")
    public CustomApiResponse logout(HttpServletRequest request,
                                    @AuthenticationPrincipal Long memberId) {
        String accessToken = tokenProvider.extractToken(request, TokenType.ACCESS_TOKEN);
        String refreshToken = tokenProvider.extractToken(request, TokenType.REFRESH_TOKEN);
        memberService.logout(accessToken, refreshToken, memberId);
        return CustomApiResponse.onSuccess();
    }

    /**
     *
     * @param loginMemberId
     * @param memberId 회원id
     * @return null
     */
    @DeleteMapping("/{memberId}")
    public CustomApiResponse deleteMember(@AuthenticationPrincipal Long loginMemberId,
                                          @PathVariable Long memberId) {
        memberService.deleteMember(loginMemberId, memberId);
        return CustomApiResponse.onSuccess();
    }

}
