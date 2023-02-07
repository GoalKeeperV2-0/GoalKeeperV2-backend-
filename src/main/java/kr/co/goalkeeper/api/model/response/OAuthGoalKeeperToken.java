package kr.co.goalkeeper.api.model.response;

import lombok.NonNull;

import javax.validation.constraints.NotBlank;

public class OAuthGoalKeeperToken extends GoalKeeperToken {
    private boolean isNewbie;
    private String nickName;
    public OAuthGoalKeeperToken(@NonNull @NotBlank String accessToken, @NonNull String refreshToken,boolean isNewbie,String nickName) {
        super(accessToken, refreshToken);
        this.isNewbie = isNewbie;
        this.nickName = nickName;
    }
}
