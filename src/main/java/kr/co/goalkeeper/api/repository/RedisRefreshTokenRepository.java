package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.exception.GoalkeeperException;
import kr.co.goalkeeper.api.model.response.ErrorMessage;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RedisRefreshTokenRepository{
    private StringRedisTemplate stringRedisTemplate;
    public RedisRefreshTokenRepository(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    public long getUserId(String refreshToken){
        ValueOperations<String, String> keyValue = stringRedisTemplate.opsForValue();
        try {
            return Long.parseLong(keyValue.get(refreshToken)+"");
        }catch (Exception e){
            ErrorMessage errorMessage = new ErrorMessage(401,"잘못된 리프레쉬 토큰입니다.");
            throw new GoalkeeperException(errorMessage);
        }
    }
    public void addRefreshToken(String refreshToken, long userId){
        ValueOperations<String, String> keyValue = stringRedisTemplate.opsForValue();
        try {
            keyValue.set(refreshToken,userId+"");
        }catch (Exception e){
            ErrorMessage errorMessage = new ErrorMessage(401,"잘못된 리프레쉬 토큰입니다.");
            throw new GoalkeeperException(errorMessage);
        }
    }
}
