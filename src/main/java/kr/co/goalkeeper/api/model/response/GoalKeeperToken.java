package kr.co.goalkeeper.api.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class GoalKeeperToken {
    @NonNull
    @NotBlank
    private String accessToken;
    @NonNull
    private String refreshToken;

    private boolean isNewbie;
    private String nickName;

    public ResponseCookie createRefreshTokenCookie(){
        return ResponseCookie.from("refreshToken",refreshToken)
                .httpOnly(true)
                .sameSite("None")
                .path("/")
                .secure(true)
                .build();
    }

}
