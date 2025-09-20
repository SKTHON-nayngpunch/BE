/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.food.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.skthon.nayngpunch.domain.food.dto.request.FoodCreateRequest;
import com.skthon.nayngpunch.domain.food.dto.response.FoodAnalysisResponse;
import com.skthon.nayngpunch.domain.food.dto.response.FoodDetailResponse;
import com.skthon.nayngpunch.domain.food.dto.response.FoodResponse;
import com.skthon.nayngpunch.domain.food.dto.response.FoodUrgentItemResponse;
import com.skthon.nayngpunch.domain.food.service.FoodService;
import com.skthon.nayngpunch.global.response.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Food", description = "음식 관리 api")
public class FoodController {

  private final FoodService foodService;

  @Operation(summary = "식재료 사진 분석", description = "사용자가 사진을 등록했을 때 요청되는 API")
  @PostMapping(path = "/analysis", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<BaseResponse<FoodAnalysisResponse>> getFoodAnalysis(
      @Parameter(description = "음식 이미지") @RequestPart(value = "foodImage")
          MultipartFile foodImage) {
    return ResponseEntity.ok(BaseResponse.success(foodService.getFoodAnalysis(foodImage)));
  }

  @Operation(summary = "food 모집글 생성", description = "게시판 페이지에서 게시글 작성 후 생성 버튼을 눌렀을 때 요청되는 API")
  @PostMapping(path = "/foods", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<BaseResponse<FoodResponse.FoodCreateResponse>> createFood(
      @Parameter(
              description = "이미지 파일",
              content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
          @RequestPart(value = "image")
          MultipartFile foodImage,
      @Parameter(
              description = "모집글 작성 내용",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
          @RequestPart(value = "request")
          @Valid
          FoodCreateRequest foodCreateRequest) {
    FoodResponse.FoodCreateResponse response = foodService.createFood(foodImage, foodCreateRequest);
    return ResponseEntity.ok(BaseResponse.success(response));
  }

  @Operation(summary = "마감 임박 목록", description = "마감 하루 전 + 남은 슬롯 ≤ 2 인 글 목록 반환")
  @GetMapping("/closing-soon/foods")
  public ResponseEntity<BaseResponse<List<FoodUrgentItemResponse>>> getClosingSoon() {
    List<FoodUrgentItemResponse> items = foodService.getClosingSoonList();
    return ResponseEntity.ok(BaseResponse.success(items));
  }

  @Operation(summary = "모집글 상세 조회", description = "foodId로 모집글 상세 조회")
  @GetMapping("/foods/{foodId}")
  public ResponseEntity<BaseResponse<FoodDetailResponse>> getDetail(@PathVariable Long foodId) {
    var res = foodService.getDetail(foodId);
    return ResponseEntity.ok(BaseResponse.success(res));
  }
}
