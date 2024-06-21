package com.simbongsa.oauth2.service;

import com.simbongsa.oauth2.dto.res.OauthLoginRes;
import com.simbongsa.oauth2.param.OauthParams;

public interface OauthService {
    OauthLoginRes getMemberByOauthLogin(OauthParams oauthParam);
}
