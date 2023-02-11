package kr.co.goalkeeper.api.service.port;

import kr.co.goalkeeper.api.model.entity.User;
import kr.co.goalkeeper.api.model.oauth.OAuthType;
import kr.co.goalkeeper.api.model.request.AdditionalUserInfo;
import kr.co.goalkeeper.api.model.request.JoinRequest;
import kr.co.goalkeeper.api.model.request.LoginRequest;
import kr.co.goalkeeper.api.model.request.OAuthRequest;
import kr.co.goalkeeper.api.model.response.GoalKeeperToken;
import org.springframework.http.ResponseCookie;

public interface CredentialService {
    GoalKeeperToken loginByEmailPassword(LoginRequest loginRequest);
    GoalKeeperToken loginByOAuth2(OAuthRequest oAuthRequest);
    GoalKeeperToken join(JoinRequest joinRequest);
    GoalKeeperToken refreshToken(String refreshToken, OAuthType oAuthType);
    ResponseCookie createRefreshTokenCookie(String refreshToken);
    void joinCompleteAfterOAuthJoin(long userId, AdditionalUserInfo additionalUserInfo);
    User getUserById(long userId);
    long getUserId(String accessToken);
}
