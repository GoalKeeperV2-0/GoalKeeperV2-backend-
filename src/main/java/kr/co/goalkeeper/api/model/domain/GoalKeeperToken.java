package kr.co.goalkeeper.api.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoalKeeperToken {
    private String accessToken;
    private String refreshToken;
}
