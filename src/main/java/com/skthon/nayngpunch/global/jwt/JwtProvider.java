/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.global.jwt;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import com.skthon.nayngpunch.domain.auth.dto.response.TokenResponse;
import com.skthon.nayngpunch.domain.auth.exception.AuthErrorCode;
import com.skthon.nayngpunch.domain.user.exception.UserErrorCode;
import com.skthon.nayngpunch.global.exception.CustomException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

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
public class JwtProvider {

  private final JwtProperties jwtProperties;

  private SecretKey key;

  private String domain;

  private final TokenRepository tokenRepository;

  public static final String TOKEN_TYPE_ACCESS = "access";

  public static final String TOKEN_TYPE_REFRESH = "refresh";

  private static final String AUTHORIZATION_HEADER = "Authorization";

  private static final String BEARER_PREFIX = "Bearer ";

  public JwtProvider(JwtProperties jwtProperties, TokenRepository tokenRepository) {
    this.jwtProperties = jwtProperties;
    this.tokenRepository = tokenRepository;
  }

  @PostConstruct
  public void init() {
    try {
      byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
      this.key = Keys.hmacShaKeyFor(keyBytes);
    } catch (Exception e) {
      log.warn("Base64 디코딩 실패, 일반 텍스트로 처리합니다: {}", e.getMessage());
      this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }
    this.domain = jwtProperties.getDomain();
    log.info("JWT key initialized");
  }

  public TokenResponse createTokens(Authentication authentication) {
    String username = authentication.getName();

    String accessToken = createToken(authentication, TOKEN_TYPE_ACCESS);

    String refreshToken = createToken(authentication, TOKEN_TYPE_REFRESH);

    tokenRepository.saveRefreshToken(username, refreshToken);

    return TokenResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .username(username)
        .build();
  }

  public String createToken(Authentication authentication, String tokenType) {
    String authorities =
        authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

    String subject;
    Object principal = authentication.getPrincipal();
    if (principal instanceof OAuth2User oAuth2User) {
      Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
      if (kakaoAccount == null || !kakaoAccount.containsKey("email")) {
        throw new CustomException(UserErrorCode.UNAUTHORIZED);
      }
      subject = (String) kakaoAccount.get("email");
    } else {
      subject = authentication.getName();
    }

    long now = System.currentTimeMillis();
    Date validity;

    if (TOKEN_TYPE_REFRESH.equals(tokenType)) {
      validity = new Date(now + jwtProperties.getRefreshTokenValidityInSeconds() * 1000);
    } else {
      validity = new Date(now + jwtProperties.getAccessTokenValidityInSeconds() * 1000);
    }

    return Jwts.builder()
        .setSubject(subject)
        .claim("auth", authorities)
        .claim("type", tokenType)
        .setIssuedAt(new Date(now))
        .setExpiration(validity)
        .signWith(key)
        .compact();
  }

  public String createToken(Authentication authentication) {
    return createToken(authentication, TOKEN_TYPE_ACCESS);
  }

  public String getUsernameFromToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  private String getTokenType(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .get("type", String.class);
  }

  public boolean validateToken(String token) {
    try {
      if (tokenRepository.isBlacklisted(token)) {
        log.info("블랙리스트에 등록된 토큰입니다.");
        return false;
      }

      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (SignatureException | MalformedJwtException e) {
      log.info("잘못된 JWT 서명입니다.");
    } catch (ExpiredJwtException e) {
      log.info("만료된 JWT 토큰입니다.");
    } catch (UnsupportedJwtException e) {
      log.info("지원되지 않는 JWT 토큰입니다.");
    } catch (IllegalArgumentException e) {
      log.info("JWT 토큰이 잘못되었습니다.");
    }
    return false;
  }

  public boolean validateTokenType(String token, String expectedType) {
    try {
      return expectedType.equals(getTokenType(token));
    } catch (Exception e) {
      throw new CustomException(AuthErrorCode.JWT_TOKEN_EXPIRED);
    }
  }

  public long getExpirationTime(String token) {
    try {
      Date expiration =
          Jwts.parserBuilder()
              .setSigningKey(key)
              .build()
              .parseClaimsJws(token)
              .getBody()
              .getExpiration();
      return (expiration.getTime() - System.currentTimeMillis()) / 1000;
    } catch (Exception e) {
      return 0;
    }
  }

  public void blacklistToken(String token) {
    long expirationTime = getExpirationTime(token);
    if (expirationTime > 0) tokenRepository.addToBlacklist(token, expirationTime);
  }

  public boolean validateRefreshToken(String username, String refreshToken) {
    String storedToken = tokenRepository.findRefreshToken(username);
    return storedToken != null && storedToken.equals(refreshToken);
  }

  public void deleteRefreshToken(String username) {
    tokenRepository.deleteRefreshToken(username);
  }

  public void addJwtToCookie(HttpServletResponse response, String token, String name, long maxAge) {
    Cookie cookie = new Cookie(name, token);
    cookie.setDomain(domain);
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setMaxAge((int) maxAge);
    response.addCookie(cookie);

    log.info("JWT 쿠키가 설정되었습니다 - 이름: {}, 만료: {}초", name, cookie.getMaxAge());
  }

  public void removeJwtCookie(HttpServletResponse response, String name) {
    Cookie cookie = new Cookie(name, null);
    cookie.setDomain(domain);
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setMaxAge(0);
    response.addCookie(cookie);

    log.info("JWT 쿠키가 삭제되었습니다 - 이름: {}", name);
  }

  public String extractAccessToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(BEARER_PREFIX.length());
    } else if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if ("ACCESS_TOKEN".equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }

  public String extractRefreshToken(HttpServletRequest request) {
    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if ("REFRESH_TOKEN".equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }
    throw new CustomException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND);
  }
}
