package com.skthon.nayngpunch.global.jwt;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TokenRepository {

    private final JwtProperties jwtProperties;
    private final RedisTemplate<String, Object> redisTemplate;


}
