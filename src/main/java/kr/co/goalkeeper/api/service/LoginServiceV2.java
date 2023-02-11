package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.exception.GoalkeeperException;
import kr.co.goalkeeper.api.model.entity.User;
import kr.co.goalkeeper.api.model.oauth.OAuthAccessToken;
import kr.co.goalkeeper.api.model.oauth.OAuthType;
import kr.co.goalkeeper.api.model.request.Email;
import kr.co.goalkeeper.api.model.request.OAuthRequest;
import kr.co.goalkeeper.api.model.request.Password;
import kr.co.goalkeeper.api.model.response.ErrorMessage;
import kr.co.goalkeeper.api.model.response.GoalKeeperToken;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class LoginServiceV2 implements LoginService {
    private final GoalKeeperTokenService goalKeeperTokenService;
    private final GoogleOAuth2Service googleOAuth2Service;
    private final UserService userService;

    public LoginServiceV2(GoalKeeperTokenService goalKeeperTokenService, GoogleOAuth2Service googleOAuth2Service, UserService userService) {
        this.goalKeeperTokenService = goalKeeperTokenService;
        this.googleOAuth2Service = googleOAuth2Service;
        this.userService = userService;
    }

    @Override
    public GoalKeeperToken loginByEmailPassword(Email email, Password password) {
        return null;
    }

    @Override
    public GoalKeeperToken loginByOAuth2(OAuthRequest oAuthRequest) {
        OAuthType oAuthType = oAuthRequest.getOAuthType();
        String code = oAuthRequest.getCode();
        String origin = oAuthRequest.getOrigin();
        switch (oAuthType){
            case GOOGLE:
                return googleLogin(code,origin);
            case NAVER:
                return naverLogin(code);
            case KAKAO:
                return kakaoLogin(code);
            default:
                ErrorMessage errorMessage =new ErrorMessage(400,"지원하지 않는 방식입니다.");
                throw new GoalkeeperException(errorMessage);
        }
    }
    private GoalKeeperToken googleLogin(String code,String origin){
        Map<String,String> credential = googleOAuth(code,origin);
        String email = credential.get("email");
        GoalKeeperToken goalKeeperToken;
        User user;
        if(isAlreadyRegistered(email)){
            user = getUserByEmail(email);
        }else {
            user = joinUseGoogleCredential(credential);
        }
        goalKeeperToken = createToken(user,OAuthType.GOOGLE);
        return goalKeeperToken;
    }
    private Map<String, String> googleOAuth(String code,String origin){
        OAuthAccessToken authAccessToken = googleOAuth2Service.getAccessToken(code,origin);
        return googleOAuth2Service.getCredential(authAccessToken);
    }
    private boolean isAlreadyRegistered(String email){
        return userService.isAlreadyRegistered(email);
    }
    private User getUserByEmail(String email){
        return userService.getUserByEmail(email);
    }
    private User joinUseGoogleCredential(Map<String,String> credential){
        User user = new User(credential,OAuthType.GOOGLE);
        userService.addUser(user);
        return user;
    }
    private GoalKeeperToken createToken(User user,OAuthType oAuthType){
        return goalKeeperTokenService.createToken(user,oAuthType);
    }

    private GoalKeeperToken naverLogin(String code){
        return null;
    }
    private GoalKeeperToken kakaoLogin(String code){
        return null;
    }

    @Override
    public GoalKeeperToken refreshToken(String refreshToken) {
        return null;
    }
}
