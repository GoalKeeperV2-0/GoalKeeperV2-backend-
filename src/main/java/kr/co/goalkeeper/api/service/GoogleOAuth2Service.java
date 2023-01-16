package kr.co.goalkeeper.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.goalkeeper.api.model.oauth.GoogleResponse;
import kr.co.goalkeeper.api.model.oauth.OAuthAccessToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
@Service
public class GoogleOAuth2Service implements OAuth2Service {
    @Value("${oauth2.client-id}")
    private String clientID;
    @Value("${oauth2.client-secret}")
    private String clientSecret;
    @Value("${oauth2.redirect-uri}")
    private String redirectURI;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public OAuthAccessToken getAccessToken(String code) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("code", code);
        parameters.put("client_id", clientID);
        parameters.put("client_secret", clientSecret);
        parameters.put("redirect_uri", redirectURI);
        parameters.put("grant_type", "authorization_code");
        ResponseEntity<GoogleResponse> response = restTemplate.postForEntity("https://oauth2.googleapis.com/token",parameters, GoogleResponse.class);
        GoogleResponse googleResponse = response.getBody();
        return new OAuthAccessToken(googleResponse.getAccess_token());
    }

    @Override
    public Map<String, String> getCredential(OAuthAccessToken authAccessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer "+authAccessToken);
        String userInfoUrl = "https://www.googleapis.com/oauth2/v1/userinfo";
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET,request,String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> result;
        try {
            result = objectMapper.readValue(userInfoResponse.getBody(), new TypeReference<>() {});
        }catch (Exception e){
            result = new HashMap<>();
        }
        return result;
    }
}
