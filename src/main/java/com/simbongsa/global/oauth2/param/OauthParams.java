package com.simbongsa.global.oauth2.param;

import com.simbongsa.global.common.constant.OauthProvider;
import org.springframework.util.MultiValueMap;

public interface OauthParams {
    public OauthProvider oauthProvider();
    public String getAuthorizationCode();
    public MultiValueMap<String, String> makeBody();
}
