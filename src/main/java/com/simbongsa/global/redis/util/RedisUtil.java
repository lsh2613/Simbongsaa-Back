package com.simbongsa.global.redis.util;

import com.simbongsa.global.common.apiPayload.code.statusEnums.ErrorStatus;
import com.simbongsa.global.common.exception.GeneralHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class RedisUtil {
    private final RedisTemplate<String, Long> redisTemplate;
    private final RedisTemplate<String, Long> redisBlackListTemplate;

    public Long getData(String key) {
        ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void setDate(String key, Long value) {
        ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    public void setDateExpire(String key, Long value, Duration duration) {
        ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, duration);
    }
    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

    public void setBlackList(String key, Long value, Long milliSeconds) {
        ValueOperations<String, Long> blackListVP = redisBlackListTemplate.opsForValue();
        blackListVP.set(key, value, milliSeconds, TimeUnit.MILLISECONDS);
    }

    public void checkTokenBlacklisted(String token) {
        if (containsInBlackList(token)) {
            throw new GeneralHandler(ErrorStatus.BLACKLISTED_TOKEN);
        }
    }

    public boolean containsInBlackList(String accessToken) {
        return redisBlackListTemplate.hasKey(accessToken);
    }
}
