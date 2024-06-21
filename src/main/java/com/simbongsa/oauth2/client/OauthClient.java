package com.simbongsa.oauth2.client;

import com.simbongsa.common.constant.OauthProvider;
import com.simbongsa.oauth2.member.OauthMember;
import com.simbongsa.oauth2.param.OauthParams;

public interface OauthClient {
    public OauthProvider oauthProvider();
    public String getOauthLoginToken(OauthParams oauthParams);

    public OauthMember getMemberInfo(String accessToken);

}
