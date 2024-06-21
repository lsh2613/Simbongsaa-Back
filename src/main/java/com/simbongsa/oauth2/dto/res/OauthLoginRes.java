package com.simbongsa.oauth2.dto.res;

import com.simbongsa.common.constant.Role;
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
