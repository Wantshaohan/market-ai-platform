package com.example.marketai.common.limiter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 基于 Redis 的滑动窗口（固定窗口）限流器。
 */
@Component
@RequiredArgsConstructor
public class RateLimiter {

    private static final String PREFIX = "rate:limit:";

    private final StringRedisTemplate redisTemplate;

    /**
     * 尝试获取许可。
     *
     * @return true 表示允许通过，false 表示触发限流
     */
    public boolean tryAcquire(String key, int limit, int windowSeconds) {
        String redisKey = PREFIX + key;
        Long count = redisTemplate.opsForValue().increment(redisKey);
        if (count != null && count == 1L) {
            redisTemplate.expire(redisKey, Duration.ofSeconds(windowSeconds));
        }
        return count != null && count <= limit;
    }
}
