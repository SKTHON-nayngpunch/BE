/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.food.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skthon.nayngpunch.domain.food.dto.request.FoodCreateRequest;
import com.skthon.nayngpunch.domain.food.dto.response.FoodResponse;
import com.skthon.nayngpunch.domain.food.service.FoodService;
import com.skthon.nayngpunch.global.response.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Food", description = "음식 관리 api")
public class FoodController {

  private final FoodService foodService;

  @Operation(summary = "food 모집글 생성", description = "게시판 페이지에서 게시글 작성 후 생성 버튼을 눌렀을 때 요청되는 API")
  @PostMapping("/foods")
  public ResponseEntity<BaseResponse<FoodResponse.FoodCreateResponse>> createFood(
      @Parameter(description = "모집글 작성 내용") @RequestBody @Valid
          FoodCreateRequest foodCreateRequest) {
    FoodResponse.FoodCreateResponse response = foodService.createFood(foodCreateRequest);
    return ResponseEntity.ok(BaseResponse.success(response));
  }
}
