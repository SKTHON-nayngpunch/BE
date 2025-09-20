/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.food.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "FoodResponse: food 응답 DTO")
public class FoodMapResponse {

  @Schema(description = "food ID", example = "1")
  private Long foodId;

  @Schema(description = "AI가 판별한 식재료 이름", example = "양파")
  private String name;

  @Schema(description = "위치(사용자 주소 그대로 노출)", example = "서울특별시 성동구 정릉동 ...")
  private String location;

  @Schema(description = "위도", example = "0.43")
  private Float latitude;

  @Schema(description = "경도", example = "3.48")
  private Float longitude;
}
