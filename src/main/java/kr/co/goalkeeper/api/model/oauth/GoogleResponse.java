package kr.co.goalkeeper.api.model.oauth;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoogleResponse {
    private String access_token;
    private String expires_in;
    private String token_type;
    private String scope;
    private String refresh_token;
}
