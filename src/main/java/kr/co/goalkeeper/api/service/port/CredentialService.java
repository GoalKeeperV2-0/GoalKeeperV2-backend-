package kr.co.goalkeeper.api.service.port;

import kr.co.goalkeeper.api.model.entity.goal.User;
import kr.co.goalkeeper.api.model.oauth.OAuthType;
import kr.co.goalkeeper.api.model.request.AdditionalUserInfo;
import kr.co.goalkeeper.api.model.request.OAuthRequest;
import kr.co.goalkeeper.api.model.request.UpdateUserRequest;
import kr.co.goalkeeper.api.model.response.GoalKeeperToken;
import org.springframework.http.ResponseCookie;

public interface CredentialService {
    GoalKeeperToken loginByOAuth2(OAuthRequest oAuthRequest);
    GoalKeeperToken refreshToken(String refreshToken, OAuthType oAuthType); //Todo 테스트 코드 작성 필요
    ResponseCookie createRefreshTokenCookie(String refreshToken);
    void joinCompleteAfterOAuthJoin(long userId, AdditionalUserInfo additionalUserInfo);
    User getUserById(long userId);
    long getUserId(String accessToken);
    User updateUser(long userId,UpdateUserRequest updateUserRequest);
}
