package kr.co.goalkeeper.api.service;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kr.co.goalkeeper.api.model.domain.GoalKeeperToken;
import kr.co.goalkeeper.api.model.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@Service
public class GoalKeeperTokenServiceImpl implements GoalKeeperTokenService {
    @Value("${jwt.key}")
    private String secretKey;
    private static final long ACCESS_TOKEN_LIFE = 60;
    public static final long REFRESH_TOKEN_LIFE = 3 * 60;

    /**
     * 내부 구조 <a href="https://bamdule.tistory.com/123">...</a> 참고
     * @param user
     * @return
     */
    @Override
    public GoalKeeperToken createToken(User user) {
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
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
        String refreshTokenString = jwtBuilder
                .setHeader(headers)
                .setSubject("access")
                .claim("userID", userId)
                .setExpiration(Timestamp.valueOf(LocalDateTime.now().plusSeconds(REFRESH_TOKEN_LIFE)))
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setIssuer("goalKeeper2")
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
        return new GoalKeeperToken(accessTokenString,refreshTokenString);
    }
}
