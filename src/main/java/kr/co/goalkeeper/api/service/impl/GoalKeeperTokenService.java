package kr.co.goalkeeper.api.service.impl;

import io.jsonwebtoken.*;
import kr.co.goalkeeper.api.exception.GoalkeeperException;
import kr.co.goalkeeper.api.model.entity.goal.User;
import kr.co.goalkeeper.api.model.oauth.OAuthType;
import kr.co.goalkeeper.api.model.response.ErrorMessage;
import kr.co.goalkeeper.api.model.response.GoalKeeperToken;
import kr.co.goalkeeper.api.repository.RedisRefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
@Service
class GoalKeeperTokenService {
    @Value("${jwt.key}")
    private String secretKey;
    private static final long ACCESS_TOKEN_LIFE = 6000000;
    public static final long REFRESH_TOKEN_LIFE = 3 * ACCESS_TOKEN_LIFE;

    private final RedisRefreshTokenRepository refreshTokenRepository;

    GoalKeeperTokenService(RedisRefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * 내부 구조 <a href="https://bamdule.tistory.com/123">...</a> 참고
     * @param user
     * @return
     */
    GoalKeeperToken createToken(User user, OAuthType oAuthType) {
        long userId = user.getId();
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");
        JwtBuilder jwtBuilder = Jwts.builder();
        String accessTokenString = jwtBuilder
                .setHeader(headers)
                .setSubject("access")
                .claim("userID", userId)
                .setExpiration(Timestamp.valueOf(LocalDateTime.now().plusSeconds(ACCESS_TOKEN_LIFE)))
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setIssuer("goalKeeper2")
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secretKey.getBytes()))
                .compact();
        String refreshTokenString = jwtBuilder
                .setHeader(headers)
                .setSubject("access")
                .claim("userID", userId)
                .setExpiration(Timestamp.valueOf(LocalDateTime.now().plusSeconds(REFRESH_TOKEN_LIFE)))
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setIssuer("goalKeeper2")
                .signWith(SignatureAlgorithm.HS256,Base64.getEncoder().encodeToString(secretKey.getBytes()))
                .compact();
        refreshTokenRepository.addRefreshToken(refreshTokenString,userId);
        return new GoalKeeperToken(accessTokenString,refreshTokenString,user.isJoinComplete(), user.getName());
    }
    GoalKeeperToken reCreateToken(String refreshToken,OAuthType oAuthType){
        try {
            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(Base64.getEncoder().encodeToString(secretKey.getBytes()))
                    .parseClaimsJws(refreshToken);
            long userIdInRedis = refreshTokenRepository.getUserId(refreshToken);
            long userIdInToken = (Integer)jws.getBody().get("userID");
            if(userIdInToken == userIdInRedis){
                User user = new User(userIdInRedis);
                refreshTokenRepository.deleteRefreshToken(refreshToken);
                return createToken(user,oAuthType);
            }else {
                ErrorMessage errorMessage = new ErrorMessage(401, "리프레쉬 토큰이 잘못되었습니다.");
                throw new GoalkeeperException(errorMessage);
            }
        }catch (ExpiredJwtException e){
            ErrorMessage errorMessage = new ErrorMessage(401, "리프레쉬 토큰이 만료되었습니다.");
            throw new GoalkeeperException(errorMessage);
        }
    }
    long getUserId(String accessToken){
        accessToken = accessToken.replace("Bearer","");
        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secretKey.getBytes()))
                .parseClaimsJws(accessToken);
        return jws.getBody().get("userID", Long.class);
    }
}
