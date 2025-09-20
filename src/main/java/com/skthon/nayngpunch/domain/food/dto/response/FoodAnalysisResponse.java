/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.food.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "FoodAnalysisResponse: 사진 분석 즉시 응답 DTO")
public class FoodAnalysisResponse {

  @Schema(description = "업로드된 이미지 URL", example = "https://s3.../img_abc.jpg")
  private String foodImageUrl;

  @Schema(description = "AI가 판별한 식재료 이름", example = "양파")
  private String name;

  @Schema(description = "AI가 산출한 신선도(1~10). 7 미만이면 나눔 불가", example = "8")
  private Integer conditionScore;

  @Schema(description = "AI 분석 내용(점수 이유)", example = "겉표면 건전, 연화/곰팡이 없음, 저장상태 양호")
  private String analysis;
}
