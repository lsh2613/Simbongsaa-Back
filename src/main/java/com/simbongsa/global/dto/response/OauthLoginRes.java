package com.simbongsa.global.dto.response;

import com.simbongsa.global.constant.Role;
import com.simbongsa.global.entity.Member;
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
