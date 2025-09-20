/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "UserResultResponse DTO", description = "사용자 나눔 정보 조회 응답")
public class UserResultResponse {

  @Schema(description = "사용자 식별자", example = "1")
  private Long userId;

  @Schema(description = "닉네임", example = "냥냥펀치")
  private String nickname;

  @Schema(description = "남은 기회 횟수", example = "1")
  private Long chanceCount;

  @Schema(description = "탄소 배출량", example = "0.38")
  private Float carbonCount;

  @Schema(description = "나눔횟수", example = "13")
  private Long shareCount;

  @Schema(description = "받은횟수", example = "냥냥펀치")
  private Long receiveCount;
}
