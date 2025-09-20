/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.food.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(title = "CreatePostRequest: 게시글 생성 요청 DTO")
public class FoodCreateRequest {

  @NotBlank(message = "제목은 비어 있을 수 없습니다.")
  @Schema(description = "모집글 제목", example = "싱싱한 양파 3개 나눔합니다!")
  private String title;

  @NotNull(message = "모집 인원은 필수입니다.")
  @Min(value = 1, message = "모집 인원은 1 이상이어야 합니다.")
  @Schema(description = "모집 인원(최대 참여 가능 인원)", example = "3")
  private Integer maxMember;

  @NotBlank(message = "내용은 비어 있을 수 없습니다.")
  @Schema(
      description = "게시글 내용",
      example =
          "안녕하세요, 저희 집 냉장고 정리를 위해 양파 3개를 나눔하려고 합니다.\n"
              + "마트에서 직접 구매한 양파라 신선도는 10점 만점에 8점 정도 되는 것 같아요. \n"
              + "사진을 보시면 아시겠지만 아주 싱싱합니다!\n"
              + "필요하신 분께 좋은 나눔이 되었으면 좋겠습니다. \n"
              + "편하게 채팅 주세요!")
  private String content;

  @NotBlank
  @Schema(description = "식재료 이름", example = "양파")
  private String name;

  @NotNull
  @Min(1)
  @Max(10)
  @Schema(description = "신선도(1~10). 7 미만이면 저장 거부", example = "8")
  private Integer conditionScore;

  @NotBlank
  @Schema(description = "AI 분석 내용(점수 사유)", example = "겉표면 건전, 연화/곰팡이 없음, 저장상태 양호")
  private String analysis;
}
