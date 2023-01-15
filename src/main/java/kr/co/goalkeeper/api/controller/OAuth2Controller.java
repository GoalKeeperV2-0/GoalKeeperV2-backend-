package kr.co.goalkeeper.api.controller;

import kr.co.goalkeeper.api.model.oauth.GoogleResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("api/oauth2/")
public class OAuth2Controller {
    @Value("${oauth2.client-id}")
    private String clientID;
    @Value("${oauth2.client-secret}")
    private String clientSecret;
    @Value("${oauth2.redirect-uri}")
    private String redirectURI;
    @GetMapping("/{snsType}")
    public ResponseEntity<?> oauth(@PathVariable("snsType") String snsType, @RequestParam String code){
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("code", code);
        parameters.put("client_id", clientID);
        parameters.put("client_secret", clientSecret);
        parameters.put("redirect_uri", redirectURI);
        parameters.put("grant_type", "authorization_code");
        ResponseEntity<GoogleResponse> response = restTemplate.postForEntity("https://oauth2.googleapis.com/token",parameters, GoogleResponse.class);

        // 아래는 검증 완료
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer "+response.getBody().getAccess_token());
        String userInfoUrl = "https://www.googleapis.com/oauth2/v1/userinfo";
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(httpHeaders);
        ResponseEntity<String> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET,request,String.class);
        System.out.println(userInfoResponse.getBody());
        return userInfoResponse;
    }
}
