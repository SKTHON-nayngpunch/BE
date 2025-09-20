/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.participation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.skthon.nayngpunch.domain.participation.dto.response.CreateParticipateResponse;
import com.skthon.nayngpunch.domain.participation.dto.response.ShareResultResponse;
import com.skthon.nayngpunch.global.response.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "참여", description = "참여 관련 API")
@RequestMapping("/api/participations")
public interface ParticipationController {

  @PostMapping
  @Operation(summary = "나눔 시작 요청", description = "사용자가 채팅 시작을 누르면 생성되는 요청")
  ResponseEntity<BaseResponse<CreateParticipateResponse>> createParticipation(
      @Parameter(description = "음식 식별자") @RequestParam Long foodId);

  @PutMapping("/sub-complete")
  @Operation(summary = "나눔자 1차 확인 요청", description = "나눔 하는 사람이 나눔 완료시 누르는 요청")
  ResponseEntity<BaseResponse<ShareResultResponse>> updateSubComplete(
      @Parameter(description = "참여 식별자", example = "1") @RequestParam Long participationId);

  @PutMapping("/complete")
  @Operation(summary = "나눔 최종 확인 요청", description = "나눔 받은 사람이 나눔 완료시 누르는 최종 요청")
  ResponseEntity<BaseResponse<ShareResultResponse>> updateComplete(
      @Parameter(description = "참여 식별자", example = "1") @RequestParam Long participationId);
}
