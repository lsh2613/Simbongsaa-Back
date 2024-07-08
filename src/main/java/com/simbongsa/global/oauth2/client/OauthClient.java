package com.simbongsa.global.oauth2.client;

import com.simbongsa.global.common.constant.OauthProvider;
import com.simbongsa.global.oauth2.member.OauthMember;
import com.simbongsa.global.oauth2.param.OauthParams;

public interface OauthClient {
    public OauthProvider oauthProvider();
    public String getOauthLoginToken(OauthParams oauthParams);

    public OauthMember getMemberInfo(String accessToken);

}
