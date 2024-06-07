package com.simbongsa.global.controller;

import com.simbongsa.global.apiPayload.CustomApiResponse;
import com.simbongsa.global.dto.request.OauthLoginReq;
import com.simbongsa.global.dto.response.OauthLoginRes;
import com.simbongsa.global.oauth2.param.AppleParams;
import com.simbongsa.global.oauth2.param.GoogleParams;
import com.simbongsa.global.oauth2.param.KakaoParams;
import com.simbongsa.global.oauth2.param.OauthParams;
import com.simbongsa.global.oauth2.service.OauthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth2")
public class OauthController {
    private final OauthService oauthService;

    @PostMapping("/login")
    public CustomApiResponse<OauthLoginRes> socialLogin(@Valid @RequestBody OauthLoginReq oauthLoginReq) {
        OauthParams oauthParam;
        switch (oauthLoginReq.getOauthProvider()) {
            case KAKAO -> oauthParam = new KakaoParams(oauthLoginReq.getCode());
            case GOOGLE -> oauthParam = new GoogleParams(oauthLoginReq.getCode());
            case APPLE -> oauthParam = new AppleParams(oauthLoginReq.getCode());
            default -> throw new IllegalStateException("Unexpected value: " + oauthLoginReq.getOauthProvider());
        }

        OauthLoginRes memberByOauthLogin = oauthService.getMemberByOauthLogin(oauthParam);
        return CustomApiResponse.onSuccess(memberByOauthLogin);
    }
}
