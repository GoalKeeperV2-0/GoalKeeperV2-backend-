package kr.co.goalkeeper.api.controller;

import kr.co.goalkeeper.api.model.response.GoalKeeperToken;
import kr.co.goalkeeper.api.model.response.Response;
import kr.co.goalkeeper.api.service.GoalKeeperTokenService;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@RestController
@RequestMapping("api/login/")
public class LoginController {
    private final GoalKeeperTokenService goalKeeperTokenService;

    public LoginController(GoalKeeperTokenService goalKeeperTokenService) {
        this.goalKeeperTokenService = goalKeeperTokenService;
    }

    @GetMapping("")
    public ResponseEntity<Response<?>> reCreateTokens(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().contentEquals("refreshToken"))
                .findFirst().orElseThrow().getValue();
        GoalKeeperToken goalKeeperToken = goalKeeperTokenService.reCreateToken(refreshToken);
        ResponseCookie cookie = goalKeeperTokenService.createRefreshTokenCookie(goalKeeperToken.getRefreshToken());
        response.addHeader("Set-Cookie",cookie.toString());
        Response<GoalKeeperToken> result = new Response<>("토큰 재발급 성공",goalKeeperToken);
        return ResponseEntity.ok(result);
    }
}
