package com.skthon.nayngpunch.global.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * JWT 토큰 생성 및 검증을 담당하는 클래스
 *
 * <p>이 클래스는 JWT 토큰의 생성, 검증, 파싱 등의 기능을 제공합니다. Access Token과 Refresh Token을 모두 지원하며, Redis를 통한 토큰 관리
 * 기능을 포함합니다.
 *
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;

    private final TokenRepository tokenRepository;
}
