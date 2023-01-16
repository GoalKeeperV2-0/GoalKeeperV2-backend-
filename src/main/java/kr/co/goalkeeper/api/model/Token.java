package kr.co.goalkeeper.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Token {
    private AccessToken accessToken;
    private RefreshToken refreshToken;
}
