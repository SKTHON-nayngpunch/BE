package com.skthon.nayngpunch.global.jwt;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JWT 관련 설정 속성
 *
 * <p>환경변수로부터 값을 읽어와서 안전하게 관리
 */
@Component
@Getter
public class JwtProperties {

    @Value("${jwt.secret}")
    private String secret;

    @Value("{jwt.access-token-validity-in-seconds}")
    private int accessTokenValidityInSeconds;

    @Value("{jwt.refresh-token-validity-in-seconds}")
    private int refreshTokenValidityInSeconds;
}
