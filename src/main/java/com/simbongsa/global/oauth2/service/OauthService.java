package com.simbongsa.global.oauth2.service;

import com.simbongsa.global.dto.response.OauthLoginRes;
import com.simbongsa.global.oauth2.param.OauthParams;

public interface OauthService {
    OauthLoginRes getMemberByOauthLogin(OauthParams oauthParam);
}
