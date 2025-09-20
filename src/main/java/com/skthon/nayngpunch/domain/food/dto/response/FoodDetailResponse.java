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
@Schema(title = "FoodDetailResponse: 모집글 상세 응답 DTO")
public class FoodDetailResponse {

  @Schema(description = "food ID", example = "12")
  private Long foodId;

  @Schema(description = "음식 사진 URL", example = "https://s3.../img.jpg")
  private String foodImageUrl;

  // 작성자 정보
  @Schema(description = "작성자 프로필 이미지 URL")
  private String writerProfileImageUrl;

  @Schema(description = "작성자 닉네임")
  private String writerNickname;

  @Schema(description = "작성자 주소 문자열")
  private String writerAddress;

  @Schema(description = "작성자 신뢰도 지수(신선도 지수)")
  private Float writerFreshScore;

  // 음식/모집 정보
  @Schema(description = "식재료 이름", example = "브로콜리")
  private String name;

  @Schema(description = "총 개수", example = "10")
  private Integer totalCount;

  @Schema(description = "제목")
  private String title;

  @Schema(description = "등록 날짜(yyyy-MM-dd)")
  private LocalDate createdDate;

  @Schema(description = "신선도 점수(1~10)")
  private Integer conditionScore;

  @Schema(description = "남은 시간(초). 등록 후 3일 기준")
  private Long remainingSeconds;

  @Schema(description = "AI 분석 내용(점수 사유)")
  private String analysis;

  @Schema(description = "내용")
  private String content;

  @Schema(description = "총 모집 인원")
  private Integer maxMember;

  @Schema(description = "현재 모인 인원 (out_count 기반)")
  private Integer currentMember;
}
