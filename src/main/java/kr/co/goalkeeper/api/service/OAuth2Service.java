package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.model.Token;

import java.util.Map;

public interface OAuth2Service {
    Token getTokens(String code);
    Map<String,Object> getCredential(Token token);
}
