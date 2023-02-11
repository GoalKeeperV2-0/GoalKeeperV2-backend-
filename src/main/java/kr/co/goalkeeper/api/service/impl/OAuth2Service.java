package kr.co.goalkeeper.api.service.impl;

import kr.co.goalkeeper.api.model.oauth.OAuthAccessToken;

import java.util.Map;

interface OAuth2Service {
    OAuthAccessToken getAccessToken(String code,String origin);
    Map<String,String> getCredential(OAuthAccessToken oAuthAccessToken);
}
