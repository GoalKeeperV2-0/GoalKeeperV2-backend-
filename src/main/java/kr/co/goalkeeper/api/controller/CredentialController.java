package kr.co.goalkeeper.api.controller;

import kr.co.goalkeeper.api.model.oauth.OAuthType;
import kr.co.goalkeeper.api.model.request.AdditionalUserInfo;
import kr.co.goalkeeper.api.model.request.JoinRequest;
import kr.co.goalkeeper.api.model.request.LoginRequest;
import kr.co.goalkeeper.api.model.request.OAuthRequest;
import kr.co.goalkeeper.api.model.response.BasicGoalKeeperToken;
import kr.co.goalkeeper.api.model.response.GoalKeeperToken;
import kr.co.goalkeeper.api.model.response.OAuthGoalKeeperToken;
import kr.co.goalkeeper.api.model.response.Response;
import kr.co.goalkeeper.api.service.port.CredentialService;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


@RestController
@RequestMapping("api/credential")
public class CredentialController {
    private final CredentialService credentialService;

    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }
    @GetMapping("/login/oauth2/{snsType}")
    public ResponseEntity<Response<OAuthGoalKeeperToken>> oauth(@PathVariable("snsType") OAuthType oAuthType, @RequestParam String code,
                                                                @RequestHeader("Origin") String origin, HttpServletResponse response){
        OAuthRequest oAuthRequest = OAuthRequest.builder()
                .oAuthType(oAuthType)
                .code(code)
                .origin(origin).build();
        OAuthGoalKeeperToken goalKeeperToken = (OAuthGoalKeeperToken) credentialService.loginByOAuth2(oAuthRequest);
        ResponseCookie cookie = goalKeeperToken.createRefreshTokenCookie();
        response.addHeader("Set-Cookie",cookie.toString());
        Response<OAuthGoalKeeperToken> responseDto = new Response<>("sns 로그인에 성공했습니다.",goalKeeperToken);
        return ResponseEntity.ok(responseDto);
    }
    @PatchMapping("/login/oauth2/additionalUserInfo")
    public ResponseEntity<?> completeJoin(@RequestBody AdditionalUserInfo userInfo, @RequestHeader("Authorization") String accessToken){
        long userId = credentialService.getUserId(accessToken);
        credentialService.joinCompleteAfterOAuthJoin(userId,userInfo);
        Response<String> response = new Response<>("sns 회원가입이 완료되었습니다.","");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Response<BasicGoalKeeperToken>> login(@RequestBody LoginRequest loginRequest,HttpServletResponse response){
        BasicGoalKeeperToken basicGoalKeeperToken = (BasicGoalKeeperToken) credentialService.loginByEmailPassword(loginRequest);
        Response<BasicGoalKeeperToken> result = new Response<>("로그인 성공",basicGoalKeeperToken);
        ResponseCookie cookie = basicGoalKeeperToken.createRefreshTokenCookie();
        response.addHeader("Set-Cookie",cookie.toString());
        return ResponseEntity.ok(result);
    }

    @PostMapping("")
    public ResponseEntity<Response<?>> join(@RequestBody JoinRequest joinRequest,HttpServletResponse response){
        GoalKeeperToken goalKeeperToken = credentialService.join(joinRequest);
        Response<GoalKeeperToken> result = new Response<>("회원가입에 성공했습니다.",goalKeeperToken);
        ResponseCookie cookie = credentialService.createRefreshTokenCookie(goalKeeperToken.getRefreshToken());
        response.addHeader("Set-Cookie",cookie.toString());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/refresh")
    public ResponseEntity<Response<?>> reCreateTokens(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().contentEquals("refreshToken"))
                .findFirst().orElseThrow().getValue();
        GoalKeeperToken goalKeeperToken = credentialService.refreshToken(refreshToken,OAuthType.NONE);
        ResponseCookie cookie = credentialService.createRefreshTokenCookie(goalKeeperToken.getRefreshToken());
        response.addHeader("Set-Cookie",cookie.toString());
        Response<GoalKeeperToken> result = new Response<>("토큰 재발급 성공",goalKeeperToken);
        return ResponseEntity.ok(result);
    }
}
