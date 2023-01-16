package kr.co.goalkeeper.api.controller;

import kr.co.goalkeeper.api.model.oauth.OAuthAccessToken;
import kr.co.goalkeeper.api.model.oauth.OAuthType;
import kr.co.goalkeeper.api.service.GoogleOAuth2Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("api/oauth2/")
@Slf4j
public class OAuth2Controller {
    @Value("${oauth2.client-id}")
    private String clientID;
    @Value("${oauth2.client-secret}")
    private String clientSecret;
    @Value("${oauth2.redirect-uri}")
    private String redirectURI;
    private final GoogleOAuth2Service googleOAuth2Service;

    public OAuth2Controller(GoogleOAuth2Service googleOAuth2Service) {
        this.googleOAuth2Service = googleOAuth2Service;
    }

    @GetMapping("/{snsType}")
    public ResponseEntity<?> oauth(@PathVariable("snsType")OAuthType oAuthType, @RequestParam String code){
        Map<String, Object> credential;
        switch (oAuthType){
            case GOOGLE:
                credential = googleOAuth(code);
                break;
            case KAKAO:
                credential = kakaoOAuth(code);
                break;
            case NAVER:
                credential = naverOAuth(code);
                break;
            default:
                throw new IllegalArgumentException("지원하지 않는 방식 입니다.");
        }
        log.info(credential.toString());
        return ResponseEntity.ok(credential);
    }
    private Map<String, Object> googleOAuth(String code){
        OAuthAccessToken authAccessToken = googleOAuth2Service.getAccessToken(code);

        return googleOAuth2Service.getCredential(authAccessToken);
    }
    private Map<String, Object> naverOAuth(String code){
        return new HashMap<>();
    }
    private Map<String, Object> kakaoOAuth(String code){
        return new HashMap<>();
    }
}
