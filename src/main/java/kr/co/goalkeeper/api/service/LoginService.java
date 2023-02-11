package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.model.request.LoginRequest;
import kr.co.goalkeeper.api.model.request.OAuthRequest;
import kr.co.goalkeeper.api.model.response.GoalKeeperToken;

public interface LoginService {
    GoalKeeperToken loginByEmailPassword(LoginRequest loginRequest);
    GoalKeeperToken loginByOAuth2(OAuthRequest oAuthRequest);
    GoalKeeperToken refreshToken(String refreshToken);
}
