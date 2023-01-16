package kr.co.goalkeeper.api.controller;

import kr.co.goalkeeper.api.model.oauth.OAuthAccessToken;
import kr.co.goalkeeper.api.model.oauth.OAuthType;
import kr.co.goalkeeper.api.service.GoogleOAuth2Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    @Autowired
    private GoogleOAuth2Service googleOAuth2Service;
    @GetMapping("/{snsType}")
    public ResponseEntity<?> oauth(@PathVariable("snsType")OAuthType oAuthType, @RequestParam String code){
        OAuthAccessToken authAccessToken = googleOAuth2Service.getAccessToken(code);

        Map<String, Object> credential = googleOAuth2Service.getCredential(authAccessToken);
        log.info(credential.toString());
        return ResponseEntity.ok(credential);
    }
}
