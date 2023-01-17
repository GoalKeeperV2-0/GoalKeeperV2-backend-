package kr.co.goalkeeper.api.model.domain;

import io.swagger.annotations.ApiParam;
import lombok.*;

@Getter
@RequiredArgsConstructor
public class GoalKeeperToken {
    @NonNull
    private String accessToken;
    @NonNull
    private String refreshToken;
    @Setter
    private boolean isNewbie;
}
