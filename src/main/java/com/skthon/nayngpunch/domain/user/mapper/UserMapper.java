/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.user.mapper;

import org.springframework.stereotype.Component;

import com.skthon.nayngpunch.domain.user.dto.request.SignUpRequest;
import com.skthon.nayngpunch.domain.user.dto.response.SignUpResponse;
import com.skthon.nayngpunch.domain.user.dto.response.UserDetailResponse;
import com.skthon.nayngpunch.domain.user.dto.response.UserResponse;
import com.skthon.nayngpunch.domain.user.entity.Role;
import com.skthon.nayngpunch.domain.user.entity.User;

@Component
public class UserMapper {

  public User toUser(SignUpRequest request, String encodedPassword, String imageUrl) {
    return User.builder()
        .username(request.getUsername())
        .password(encodedPassword)
        .nickname(request.getNickname())
        .profileImageUrl(imageUrl)
        .role(Role.ROLE_USER)
        .isDeleted(false)
        .build();
  }

  public SignUpResponse toSignUpResponse(User user) {
    return SignUpResponse.builder().userId(user.getId()).username(user.getUsername()).build();
  }

  public UserDetailResponse toUserDetailResponse(User user) {
    return UserDetailResponse.builder()
        .userId(user.getId())
        .username(user.getUsername())
        .nickname(user.getNickname())
        .profileImageUrl(user.getProfileImageUrl())
        .role(user.getRole())
        .build();
  }

  public UserResponse toUserResponse(User user) {
    return UserResponse.builder()
        .userId(user.getId())
        .nickname(user.getNickname())
        .profileImageUrl(user.getProfileImageUrl())
        .build();
  }
}
