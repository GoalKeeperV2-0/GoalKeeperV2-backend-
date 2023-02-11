package kr.co.goalkeeper.api.service.impl;

import kr.co.goalkeeper.api.exception.GoalkeeperException;
import kr.co.goalkeeper.api.model.entity.User;
import kr.co.goalkeeper.api.model.oauth.OAuthAccessToken;
import kr.co.goalkeeper.api.model.oauth.OAuthType;
import kr.co.goalkeeper.api.model.request.AdditionalUserInfo;
import kr.co.goalkeeper.api.model.request.JoinRequest;
import kr.co.goalkeeper.api.model.request.LoginRequest;
import kr.co.goalkeeper.api.model.request.OAuthRequest;
import kr.co.goalkeeper.api.model.response.ErrorMessage;
import kr.co.goalkeeper.api.model.response.GoalKeeperToken;
import kr.co.goalkeeper.api.repository.UserRepository;
import kr.co.goalkeeper.api.service.port.CredentialService;
import kr.co.goalkeeper.api.util.PasswordManager;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Map;

import static kr.co.goalkeeper.api.model.entity.User.EMPTYUSER;

@Service
class SimpleCredentialService implements CredentialService {
    private final GoalKeeperTokenService goalKeeperTokenService;
    private final GoogleOAuth2Service googleOAuth2Service;
    private final UserRepository userRepository;

    public SimpleCredentialService(GoalKeeperTokenService goalKeeperTokenService, GoogleOAuth2Service googleOAuth2Service, UserRepository userRepository) {
        this.goalKeeperTokenService = goalKeeperTokenService;
        this.googleOAuth2Service = googleOAuth2Service;
        this.userRepository = userRepository;
    }

    @Override
    public GoalKeeperToken loginByEmailPassword(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        password = PasswordManager.sha256(password);
        if (password == null) {
            ErrorMessage errorMessage = new ErrorMessage(500, "암호화 오류!");
            throw new GoalkeeperException(errorMessage);
        }
        User user = getUserByEmailAndPassword(email, password);
        return goalKeeperTokenService.createToken(user, OAuthType.NONE);
    }
    private User getUserByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password).orElseThrow(() -> {
            ErrorMessage errorMessage = new ErrorMessage(404, "이메일이나 비밀번호가 틀렸습니다.");
            return new GoalkeeperException(errorMessage);
        });
    }

    @Override
    public GoalKeeperToken loginByOAuth2(OAuthRequest oAuthRequest) {
        OAuthType oAuthType = oAuthRequest.getOAuthType();
        String code = oAuthRequest.getCode();
        String origin = oAuthRequest.getOrigin();
        switch (oAuthType) {
            case GOOGLE:
                return googleLogin(code, origin);
            case NAVER:
                return naverLogin(code);
            case KAKAO:
                return kakaoLogin(code);
            default:
                ErrorMessage errorMessage = new ErrorMessage(400, "지원하지 않는 방식입니다.");
                throw new GoalkeeperException(errorMessage);
        }
    }
    private GoalKeeperToken googleLogin(String code, String origin) {
        Map<String, String> credential = googleOAuth(code, origin);
        String email = credential.get("email");
        GoalKeeperToken goalKeeperToken;
        User user;
        if (isAlreadyRegistered(email)) {
            user = getUserByEmail(email);
        } else {
            user = joinUseGoogleCredential(credential);
        }
        goalKeeperToken = createToken(user, OAuthType.GOOGLE);
        return goalKeeperToken;
    }
    private Map<String, String> googleOAuth(String code, String origin) {
        OAuthAccessToken authAccessToken = googleOAuth2Service.getAccessToken(code, origin);
        return googleOAuth2Service.getCredential(authAccessToken);
    }
    private boolean isAlreadyRegistered(String email) {
        User user = userRepository.findByEmail(email).orElse(EMPTYUSER);
        return !user.equals(EMPTYUSER);
    }
    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> {
            ErrorMessage errorMessage = new ErrorMessage(404, "이메일이 잘못되었습니다.");
            return new GoalkeeperException(errorMessage);
        });
    }
    private User joinUseGoogleCredential(Map<String, String> credential) {
        User user = new User(credential, OAuthType.GOOGLE);
        addUser(user);
        return user;
    }
    private void addUser(User user) {
        userRepository.save(user);
    }
    private GoalKeeperToken createToken(User user, OAuthType oAuthType) {
        return goalKeeperTokenService.createToken(user, oAuthType);
    }
    private GoalKeeperToken naverLogin(String code) {
        return null;
    }
    private GoalKeeperToken kakaoLogin(String code) {
        return null;
    }

    @Override
    public GoalKeeperToken join(JoinRequest joinRequest) {
        if (isAlreadyRegistered(joinRequest.getEmail())) {
            ErrorMessage errorMessage = new ErrorMessage(409, "이미 가입된 이메일 입니다.");
            throw new GoalkeeperException(errorMessage);
        }
        User user = new User(joinRequest);
        addUser(user);
        user = getUserByEmail(joinRequest.getEmail());
        return goalKeeperTokenService.createToken(user, OAuthType.NONE);
    }

    @Override
    public GoalKeeperToken refreshToken(String refreshToken,OAuthType oAuthType) {
        return goalKeeperTokenService.reCreateToken(refreshToken,oAuthType);
    }

    @Override
    public ResponseCookie createRefreshTokenCookie(String refreshToken){
        return ResponseCookie.from("refreshToken",refreshToken)
                .httpOnly(true)
                .sameSite("None")
                .path("/")
                .secure(true)
                .build();
    }

    @Override
    public void joinCompleteAfterOAuthJoin(long userId, AdditionalUserInfo additionalUserInfo) {
        User user = getUserById(userId);
        completeJoin(user, additionalUserInfo);
    }
    private void completeJoin(User user, AdditionalUserInfo userInfo) {
        if (!user.isJoinComplete()) {
            user.setAdditional(userInfo);
            user.joinComplete();
        }
        userRepository.save(user);
    }

    @Override
    public User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    @Override
    public long getUserId(String accessToken) {
        return goalKeeperTokenService.getUserId(accessToken);
    }
}
