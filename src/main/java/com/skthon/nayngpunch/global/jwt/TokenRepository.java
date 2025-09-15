package com.skthon.nayngpunch.global.jwt;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TokenRepository {

    private final JwtProperties jwtProperties;
    private final RedisTemplate<String, Object> redisTemplate;

    /** Refresh Token Redis 키 접두사 */
    private static final String REFRESH_TOKEN_PREFIX = "RT:";

    /** 블랙리스트 Redis 키 접두사 */
    private static final String BLACKLIST_PREFIX = "BL:";

    public void saveRefreshToken(String username, String refreshToken) {
        String key = REFRESH_TOKEN_PREFIX + username;
        redisTemplate
                .opsForValue()
                .set(key, refreshToken, jwtProperties.getRefreshTokenValidityInSeconds(), TimeUnit.SECONDS);
        log.debug("Refresh Token saved for user: {}", username);
    }

    public String findRefreshToken(String username) {
        String key = REFRESH_TOKEN_PREFIX + username;
        Object token = redisTemplate.opsForValue().get(key);
        return token != null ? token.toString() : null;
    }

    public void deleteRefreshToken(String username) {
        String key = REFRESH_TOKEN_PREFIX + username;
        redisTemplate.delete(key);
        log.debug("Refresh Token deleted for user: {}", username);
    }

    public void addToBlacklist(String token, long expiration) {
        String key = BLACKLIST_PREFIX + token;
        redisTemplate.opsForValue().set(key, "blacklisted", expiration, TimeUnit.SECONDS);
    }

    public boolean isBlacklisted(String token) {
        String key = BLACKLIST_PREFIX + token;
        return redisTemplate.hasKey(key);
    }
}
