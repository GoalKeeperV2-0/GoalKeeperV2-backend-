package kr.co.goalkeeper.api.model.response;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;

import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor
public abstract class GoalKeeperToken {
    @NonNull
    @NotBlank
    private String accessToken;
    @NonNull
    private String refreshToken;

    public ResponseCookie createRefreshTokenCookie(){
        return ResponseCookie.from("refreshToken",refreshToken)
                .httpOnly(true)
                .sameSite("None")
                .path("/")
                .secure(true)
                .build();
    }

}
