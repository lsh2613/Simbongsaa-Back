package com.simbongsa.global.oauth2.dto.res;

import com.simbongsa.global.common.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OauthLoginRes {
    private Long id;
    private Role role;
    private String accessToken;
    private String refreshToken;
}
