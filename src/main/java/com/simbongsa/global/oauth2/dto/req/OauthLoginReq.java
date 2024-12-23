package com.simbongsa.global.oauth2.dto.req;

import com.simbongsa.global.common.constant.OauthProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OauthLoginReq {
    @NotNull
    private OauthProvider oauthProvider;
    @NotBlank
    private String code;
}
