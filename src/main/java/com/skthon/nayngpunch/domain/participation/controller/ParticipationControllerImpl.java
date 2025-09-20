/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.participation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.skthon.nayngpunch.domain.participation.dto.response.CreateParticipateResponse;
import com.skthon.nayngpunch.domain.participation.dto.response.ShareResultResponse;
import com.skthon.nayngpunch.domain.participation.service.ParticipationServiceImpl;
import com.skthon.nayngpunch.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ParticipationControllerImpl implements ParticipationController {

  private final ParticipationServiceImpl participationService;

  @Override
  public ResponseEntity<BaseResponse<CreateParticipateResponse>> createParticipation(
      @RequestParam Long foodId) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(
            BaseResponse.success(
                200, "참여에 성공했습니다.", participationService.createParticipate(foodId)));
  }

  @Override
  public ResponseEntity<BaseResponse<ShareResultResponse>> updateSubComplete(
      @RequestParam Long participationId) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(
            BaseResponse.success(
                200, "참여 1차 요청에 성공했습니다.", participationService.updateSubComplete(participationId)));
  }

  @Override
  public ResponseEntity<BaseResponse<ShareResultResponse>> updateComplete(
      @RequestParam Long participationId) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(
            BaseResponse.success(
                200, "참여 최종 요청에 성공했습니다.", participationService.updateComplete(participationId)));
  }
}
