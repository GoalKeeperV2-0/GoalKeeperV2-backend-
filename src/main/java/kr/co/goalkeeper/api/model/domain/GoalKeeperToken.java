package kr.co.goalkeeper.api.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GoalKeeperToken {
    private AccessToken accessToken;
    private RefreshToken refreshToken;
}
