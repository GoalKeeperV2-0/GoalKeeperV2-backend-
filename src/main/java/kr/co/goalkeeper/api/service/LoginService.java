package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.model.oauth.OAuthType;
import kr.co.goalkeeper.api.model.request.Email;
import kr.co.goalkeeper.api.model.request.OAuthRequest;
import kr.co.goalkeeper.api.model.request.Password;
import kr.co.goalkeeper.api.model.response.GoalKeeperToken;

public interface LoginService {
    GoalKeeperToken loginByEmailPassword(Email email, Password password);
    GoalKeeperToken loginByOAuth2(OAuthRequest oAuthRequest);
    GoalKeeperToken refreshToken(String refreshToken);
}
