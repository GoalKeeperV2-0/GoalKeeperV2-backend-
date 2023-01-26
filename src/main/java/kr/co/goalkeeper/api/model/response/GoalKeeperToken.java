package kr.co.goalkeeper.api.model.response;

import io.swagger.annotations.ApiParam;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor
public class GoalKeeperToken {
    @NonNull
    @NotBlank
    private String accessToken;
    @NonNull
    private String refreshToken;
    @Setter
    private boolean isNewbie;

}
