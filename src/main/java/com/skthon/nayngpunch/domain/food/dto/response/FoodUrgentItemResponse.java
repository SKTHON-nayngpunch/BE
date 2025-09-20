/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.food.dto.response;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "FoodUrgentItemResponse: 마감 임박 글 단건 응답 DTO")
public class FoodUrgentItemResponse {
  @Schema(description = "food ID", example = "12")
  private Long foodId;

  @Schema(description = "음식 사진 URL", example = "https://s3.../img_abc.jpg")
  private String foodImageUrl;

  @Schema(description = "등록 날짜(yyyy-MM-dd)", example = "2025-09-20")
  private LocalDate createdDate;

  @Schema(description = "제목", example = "브로콜리 10개 남았어요~ 세척해서 빨리 먹어야 해요")
  private String title;

  @Schema(description = "위치(사용자 주소 그대로 노출)", example = "서울특별시 성동구 정릉동 ...")
  private String location;

  @Schema(description = "식재료 이름", example = "브로콜리")
  private String name;

  @Schema(description = "총 개수", example = "10")
  private Integer totalCount;

  @Schema(description = "신선도 (1~10)", example = "8")
  private Integer conditionScore;

  @Schema(description = "총 모집 인원", example = "4")
  private Integer maxMember;

  @Schema(description = "현재 모집된 인원", example = "3")
  private Integer currentMember;

  @Schema(description = "남은 시간(초)", example = "63480")
  private Long remainingSeconds;
}
