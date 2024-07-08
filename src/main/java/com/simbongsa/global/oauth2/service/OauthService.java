package com.simbongsa.global.oauth2.service;

import com.simbongsa.global.oauth2.dto.res.OauthLoginRes;
import com.simbongsa.global.oauth2.param.OauthParams;

public interface OauthService {
    OauthLoginRes getMemberByOauthLogin(OauthParams oauthParam);
}
