package kr.co.goalkeeper.api.model.response;

import lombok.NonNull;

import javax.validation.constraints.NotBlank;

public class BasicGoalKeeperToken extends GoalKeeperToken {
    public BasicGoalKeeperToken(@NonNull @NotBlank String accessToken, @NonNull String refreshToken) {
        super(accessToken, refreshToken);
    }
}
