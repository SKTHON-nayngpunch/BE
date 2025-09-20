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
@Schema(title = "FoodResponse: food 응답 DTO")
public class FoodResponse {
  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(title = "FoodCreateResponse: food 생성 응답 DTO")
  public static class FoodCreateResponse {
    @Schema(description = "food ID", example = "1")
    private Long foodId;

    @Schema(description = "모집글 제목", example = "싱싱한 양파 3개 나눔합니다!")
    private String title;

    @Schema(description = "모집 인원(최대 참여 가능 인원)", example = "3")
    private Integer maxMember;

    @Schema(description = "게시글 내용", example = "안녕하세요, 저희 집 냉장고 정리를 위해 양파 3개를 나눔하려고 합니다...")
    private String content;

    @Schema(description = "업로드된 이미지 URL", example = "https://s3.../img_abc.jpg")
    private String foodImageUrl;

    @Schema(description = "AI가 판별한 식재료 이름", example = "양파")
    private String name;

    @Schema(description = "신선도 점수 (1~10)", example = "8")
    private Integer conditionScore;

    @Schema(description = "AI 분석 내용(점수 사유)", example = "겉표면 건전, 연화/곰팡이 없음, 저장상태 양호")
    private String analysis;
  }
}
