package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.model.oauth.OAuthAccessToken;

import java.util.Map;

public interface OAuth2Service {
    OAuthAccessToken getAccessToken(String code,String origin);
    Map<String,String> getCredential(OAuthAccessToken oAuthAccessToken);
}
