package kr.co.goalkeeper.api.controller;

import kr.co.goalkeeper.api.model.entity.User;
import kr.co.goalkeeper.api.model.oauth.OAuthType;
import kr.co.goalkeeper.api.model.request.AdditionalUserInfo;
import kr.co.goalkeeper.api.model.request.OAuthRequest;
import kr.co.goalkeeper.api.model.response.GoalKeeperToken;
import kr.co.goalkeeper.api.model.response.OAuthGoalKeeperToken;
import kr.co.goalkeeper.api.model.response.Response;
import kr.co.goalkeeper.api.service.GoalKeeperTokenService;
import kr.co.goalkeeper.api.service.LoginService;
import kr.co.goalkeeper.api.service.UserService;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@RestController
@RequestMapping("api/login/")
public class LoginController {
    private final GoalKeeperTokenService goalKeeperTokenService;
    private final LoginService loginService;
    private final UserService userService;

    public LoginController(GoalKeeperTokenService goalKeeperTokenService, LoginService loginService, UserService userService) {
        this.goalKeeperTokenService = goalKeeperTokenService;
        this.loginService = loginService;
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<Response<?>> reCreateTokens(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().contentEquals("refreshToken"))
                .findFirst().orElseThrow().getValue();
        GoalKeeperToken goalKeeperToken = goalKeeperTokenService.reCreateToken(refreshToken,OAuthType.NONE);
        ResponseCookie cookie = goalKeeperTokenService.createRefreshTokenCookie(goalKeeperToken.getRefreshToken());
        response.addHeader("Set-Cookie",cookie.toString());
        Response<GoalKeeperToken> result = new Response<>("토큰 재발급 성공",goalKeeperToken);
        return ResponseEntity.ok(result);
    }
    @GetMapping("oauth2/{snsType}")
    public ResponseEntity<Response<OAuthGoalKeeperToken>> oauth(@PathVariable("snsType") OAuthType oAuthType, @RequestParam String code,
                                                                @RequestHeader("Origin") String origin, HttpServletResponse response){
        OAuthRequest oAuthRequest = OAuthRequest.builder()
                .oAuthType(oAuthType)
                .code(code)
                .origin(origin).build();
        OAuthGoalKeeperToken goalKeeperToken = (OAuthGoalKeeperToken) loginService.loginByOAuth2(oAuthRequest);
        ResponseCookie cookie = goalKeeperToken.createRefreshTokenCookie();
        response.addHeader("Set-Cookie",cookie.toString());
        Response<OAuthGoalKeeperToken> responseDto = new Response<>("sns 로그인에 성공했습니다.",goalKeeperToken);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("oauth2/additionalUserInfo")
    public ResponseEntity<?> completeJoin(@RequestBody AdditionalUserInfo userInfo, @RequestHeader("Authorization") String accessToken){
        long userId = goalKeeperTokenService.getUserId(accessToken);
        User user = userService.getUserById(userId);
        userService.completeJoin(user,userInfo);
        Response<String> response = new Response<>("sns 회원가입이 완료되었습니다.","");
        return ResponseEntity.ok(response);
    }
}
