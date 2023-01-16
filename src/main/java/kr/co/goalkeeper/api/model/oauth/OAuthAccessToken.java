package kr.co.goalkeeper.api.model.oauth;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OAuthAccessToken {
    private String token;

    @Override
    public String toString() {
        return token;
    }
}
