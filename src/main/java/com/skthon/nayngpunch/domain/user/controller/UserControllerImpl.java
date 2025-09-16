/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.user.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.skthon.nayngpunch.domain.user.dto.request.SignUpRequest;
import com.skthon.nayngpunch.domain.user.dto.response.SignUpResponse;
import com.skthon.nayngpunch.domain.user.dto.response.UserDetailResponse;
import com.skthon.nayngpunch.domain.user.dto.response.UserResponse;
import com.skthon.nayngpunch.domain.user.service.UserService;
import com.skthon.nayngpunch.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

  private final UserService userService;

  @Override
  public ResponseEntity<BaseResponse<SignUpResponse>> signUp(
      @RequestPart("request") @Valid SignUpRequest signUpRequest,
      @RequestPart("image") MultipartFile image) {

    SignUpResponse signUpResponse = userService.signUp(signUpRequest, image);

    return ResponseEntity.ok(BaseResponse.success(201, "회원가입에 성공하였습니다.", signUpResponse));
  }

  @Override
  public ResponseEntity<BaseResponse<UserResponse>> getUser() {

    return ResponseEntity.ok(BaseResponse.success(userService.getUser()));
  }

  @Override
  public ResponseEntity<BaseResponse<UserResponse>> getUserProfile(
      @PathVariable(value = "id") Long userId) {
    return ResponseEntity.status(200)
        .body(BaseResponse.success(userService.getUserProfile(userId)));
  }

  @Override
  public ResponseEntity<BaseResponse<String>> updateProfileImage(
      @RequestPart MultipartFile profileImage) {

    return ResponseEntity.ok(BaseResponse.success(userService.updateProfileImage(profileImage)));
  }

  @Override
  public ResponseEntity<BaseResponse<List<UserDetailResponse>>> getAllUsers() {

    List<UserDetailResponse> userDetailResponses = userService.getAllUsers();

    return ResponseEntity.ok(BaseResponse.success(userDetailResponses));
  }
}
