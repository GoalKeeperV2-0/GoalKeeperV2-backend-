package kr.co.goalkeeper.api.controller;

import io.swagger.annotations.*;
import kr.co.goalkeeper.api.model.request.AdditionalUserInfo;
import kr.co.goalkeeper.api.model.response.GoalKeeperToken;
import kr.co.goalkeeper.api.model.entity.User;
import kr.co.goalkeeper.api.model.oauth.OAuthAccessToken;
import kr.co.goalkeeper.api.model.oauth.OAuthType;
import kr.co.goalkeeper.api.service.GoalKeeperTokenService;
import kr.co.goalkeeper.api.service.GoogleOAuth2Service;
import kr.co.goalkeeper.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Api(tags = {"OAuth2.0 기반의 소셜 로그인을 구현하는 컨트롤러"})
@RestController
@RequestMapping("api/oauth2/")
@Slf4j
public class OAuth2Controller {
    private final GoogleOAuth2Service googleOAuth2Service;
    private final UserService userService;
    private final GoalKeeperTokenService goalKeeperTokenService;

    public OAuth2Controller(GoogleOAuth2Service googleOAuth2Service, UserService userService, GoalKeeperTokenService goalKeeperTokenService) {
        this.googleOAuth2Service = googleOAuth2Service;
        this.userService = userService;
        this.goalKeeperTokenService = goalKeeperTokenService;
    }

    @GetMapping("/{snsType}")
    @ApiOperation(value = "OAuth 2.0 소셜 로그인", notes = "응답의 isNewbie 필드는 성별, 연령대 등 추가 정보 입력이 필요한 상태인지 나타내는 필드이다. 소셜 로그인으로 회원가입한 회원의 경우만 이 필드가 true일 수 있다.")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "code", value = "임시 인증 코드", required = true, dataType = "String", paramType = "query")
    )
    public ResponseEntity<GoalKeeperToken> oauth(@PathVariable("snsType")OAuthType oAuthType, @RequestParam String code, @RequestHeader("Origin") String origin, HttpServletResponse response){
        GoalKeeperToken goalKeeperToken;
        switch (oAuthType){
            case GOOGLE:
                goalKeeperToken = googleLogin(code,origin);
                break;
            case KAKAO:
                goalKeeperToken = kakaoLogin(code);
                break;
            case NAVER:
                goalKeeperToken = naverLogin(code);
                break;
            default:
                throw new IllegalArgumentException("지원하지 않는 방식 입니다.");
        }
        ResponseCookie cookie = goalKeeperTokenService.createRefreshTokenCookie(goalKeeperToken.getRefreshToken());
        response.addHeader("Set-Cookie",cookie.toString());
        return ResponseEntity.ok(goalKeeperToken);
    }
    private GoalKeeperToken googleLogin(String code,String origin){
        Map<String,String> credential = googleOAuth(code,origin);
        String email = credential.get("email");
        GoalKeeperToken goalKeeperToken;
        User user;
        boolean isNewbie;
        if(isAlreadyRegistered(email)){
            user = getUserByEmail(email);
        }else {
            user = joinUseGoogleCredential(credential);
        }
        isNewbie = user.isJoinComplete();
        goalKeeperToken = createToken(user);
        goalKeeperToken.setNewbie(!isNewbie);
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
        User user = User.builder()
                .email(credential.get("email"))
                .name(credential.get("name"))
                .picture(credential.get("picture"))
                .point(500)
                .joinComplete(false)
                .build();
        userService.addUser(user);
        return user;
    }
    private GoalKeeperToken createToken(User user){
        return goalKeeperTokenService.createToken(user);
    }

    private GoalKeeperToken naverLogin(String code){
        return null;
    }
    private GoalKeeperToken kakaoLogin(String code){
        return null;
    }

    @PatchMapping("additionalUserInfo")
    @ApiOperation(value = "추가 회원 정보 입력", notes = "제공하지 않는 추가 정보는 바디에 아예 포함하지 않거나 null 로 지정한다.")
    public ResponseEntity<?> completeJoin(@RequestBody AdditionalUserInfo userInfo,
                                          @RequestHeader("Authorization") String accessToken){
        long userId = goalKeeperTokenService.getUserId(accessToken);
        User user = userService.getUserById(userId);
        userService.completeJoin(user,userInfo);
        return ResponseEntity.ok().build();
    }
}
