package kr.co.goalkeeper.api.model.request;

import kr.co.goalkeeper.api.model.oauth.OAuthType;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
@Builder
@Getter
public class OAuthRequest {
    @NonNull
    private OAuthType oAuthType;
    @NonNull
    private String code;
    private String origin;
}
