package kr.co.goalkeeper.api.model.response;

import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class OAuthGoalKeeperToken extends GoalKeeperToken {
    private boolean isNewbie;
    public OAuthGoalKeeperToken(@NonNull @NotBlank String accessToken, @NonNull String refreshToken,boolean isNewbie) {
        super(accessToken, refreshToken);
        this.isNewbie = isNewbie;
    }
}
