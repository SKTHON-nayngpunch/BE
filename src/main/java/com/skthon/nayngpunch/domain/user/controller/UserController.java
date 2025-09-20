/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.user.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.skthon.nayngpunch.domain.user.dto.request.SignUpRequest;
import com.skthon.nayngpunch.domain.user.dto.request.UpdateUserRequest;
import com.skthon.nayngpunch.domain.user.dto.response.SignUpResponse;
import com.skthon.nayngpunch.domain.user.dto.response.UserDetailResponse;
import com.skthon.nayngpunch.domain.user.dto.response.UserResponse;
import com.skthon.nayngpunch.domain.user.dto.response.UserResultResponse;
import com.skthon.nayngpunch.global.response.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "사용자", description = "사용자 관련 API")
@RequestMapping("/api/users")
public interface UserController {

  @PostMapping(value = "/sign-up", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "회원가입", description = "서비스 자체 회원을 위한 회원가입을 처리합니다.")
  ResponseEntity<BaseResponse<SignUpResponse>> signUp(
      @Parameter(
              description = "회원가입 정보",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
          @RequestPart(value = "request")
          @Valid
          SignUpRequest signUpRequest,
      @Parameter(
              description = "프로필 이미지",
              content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
          @RequestPart(value = "image")
          MultipartFile image);

  @GetMapping
  @Operation(summary = "사용자 정보 조회", description = "현재 로그인된 사용자의 정보를 조회합니다.")
  ResponseEntity<BaseResponse<UserResponse>> getUser();

  @GetMapping("/{id}/profile")
  @Operation(summary = "사용자 프로필 조회", description = "사용자의 프로필 사진, 닉네임을 조회합니다.")
  ResponseEntity<BaseResponse<UserResponse>> getUserProfile(
      @Parameter(description = "특정 유저 식별자") @PathVariable(value = "id") Long userId);

  @PutMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "프로필 사진 변경", description = "현재 로그인된 사용자의 프로필 사진을 변경합니다.")
  ResponseEntity<BaseResponse<String>> updateProfileImage(
      @Parameter(description = "새로운 프로필 사진") @RequestPart MultipartFile profileImage);

  @GetMapping("/devs")
  @Operation(summary = "[개발자]사용자 전체 조회", description = "스웨거를 사용해 전체 사용자를 조회합니다.")
  ResponseEntity<BaseResponse<List<UserDetailResponse>>> getAllUsers();

  @PutMapping("/profile")
  @Operation(summary = "사용자 정보 수정", description = "로그인 후 사용자 정보 수정")
  ResponseEntity<BaseResponse<String>> updateUserProfile(
      @Parameter(description = "사용자 수정 정보") @RequestBody @Valid
          UpdateUserRequest updateUserRequest);

  @GetMapping("/result")
  @Operation(summary = "나눔과 관련된 사용자의 정보 조회", description = "메인화면에서 나눔과 관련된 사용자의 정보 조회")
  ResponseEntity<BaseResponse<UserResultResponse>> getUserResult();

  //  @GetMapping("/shares")
  //  @Operation(summary = "나눔과 관련된 채팅 목록 조회", description = "채팅 화면에서 진행 중인 나눔과 관련된 리스트 조회")
  //  ResponseEntity<BaseResponse<List<UserResponse>>> getUserShares();
}
