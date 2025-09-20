/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.participation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "LoginRequest DTO", description = "사용자 로그인을 위한 데이터 전송")
public class CreateParticipateRequest {

  @Schema(description = "참여자 식별자", example = "1")
  private String userId;

  @Schema(description = "음식 식별자", example = "1")
  private String foodId;
}
