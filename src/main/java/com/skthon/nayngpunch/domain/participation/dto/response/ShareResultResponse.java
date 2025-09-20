/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.participation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "나눔 결과 응답")
public class ShareResultResponse {
  @Schema(description = "참여 식별자", example = "1")
  private Long participateId;

  @Schema(description = "사용자 식별자", example = "1")
  private Long userId;

  @Schema(description = "사용자 닉네임", example = "1")
  private String nickname;

  @Schema(description = "이번 거래 탄소배출 절약량", example = "0.28")
  private Float carbonCount;

  @Schema(description = "해당 사용자의 탄소배출 절약량", example = "3.38")
  private Float totalCarbonCount;
}
