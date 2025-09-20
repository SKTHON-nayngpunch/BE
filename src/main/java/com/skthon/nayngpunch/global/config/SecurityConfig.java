/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.global.config;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import com.skthon.nayngpunch.global.security.CustomUserDetails;
import com.skthon.nayngpunch.global.security.JwtAuthenticationFilter;
import com.skthon.nayngpunch.global.security.OAuth2LoginSuccessHandler;
import com.skthon.nayngpunch.global.security.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CorsConfig corsConfig;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final CustomOAuth2UserService customOAuth2UserService;
  private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    configureFilters(http);
    configureExceptionHandling(http);
    configureAuthorization(http);
    configureOAuth2(http);
    return http.build();
  }

  private void configureFilters(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
  }

  private void configureExceptionHandling(HttpSecurity http) throws Exception {
    http.exceptionHandling(
        e ->
            e.authenticationEntryPoint(this::handleAuthException)
                .accessDeniedHandler(this::handleAccessDenied));
  }

  private void handleAuthException(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json;charset=UTF-8");
    response
        .getWriter()
        .write("{\"success\": false, \"code\": 401, \"message\": \"JWT 토큰이 없거나 유효하지 않습니다.\"}");
    log.warn("인증 실패: {} {}", request.getMethod(), request.getRequestURI());
  }

  private void handleAccessDenied(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException {
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType("application/json;charset=UTF-8");

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String userId = "anonymous";

    if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
      Object principal = auth.getPrincipal();
      if (principal instanceof CustomUserDetails) {
        userId = ((CustomUserDetails) principal).getUser().getId().toString();
      } else if (principal instanceof OAuth2User) {
        Object idAttr = ((OAuth2User) principal).getAttributes().get("id");
        if (idAttr != null) userId = idAttr.toString();
      }
    }

    response
        .getWriter()
        .write("{\"success\": false, \"code\": 403, \"message\": \"접근 권한이 없습니다.\"}");
    log.warn("권한 부족: {} {}, userId={}", request.getMethod(), request.getRequestURI(), userId);
  }

  private void configureAuthorization(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
        auth ->
            auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
                .hasRole("USER")
                .requestMatchers(
                    "/api/users/sign-up",
                    "/api/auths/login",
                    "/api/auths/test-login",
                    "/actuator/health")
                .permitAll()
                .requestMatchers("/error")
                .permitAll()
                .requestMatchers(RegexRequestMatcher.regexMatcher(".*/admin/.*"))
                .hasRole("ADMIN")
                .requestMatchers(RegexRequestMatcher.regexMatcher(".*/dev/.*"))
                .hasRole("DEVELOPER")
                .anyRequest()
                .authenticated());
  }

  private void configureOAuth2(HttpSecurity http) throws Exception {
    http.oauth2Login(
        oauth2 ->
            oauth2
                .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                .successHandler(oAuth2LoginSuccessHandler));
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
}
