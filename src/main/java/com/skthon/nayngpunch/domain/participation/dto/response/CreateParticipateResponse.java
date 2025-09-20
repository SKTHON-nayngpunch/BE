/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.participation.dto.response;

import com.skthon.nayngpunch.domain.participation.entity.ParticipationStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "참여 응답")
public class CreateParticipateResponse {

  @Schema(description = "참여 식별자", example = "1")
  private Long participatationId;

  @Schema(description = "참여자 식별자", example = "1")
  private Long userId;

  @Schema(description = "음식 식별자", example = "1")
  private Long foodId;

  @Schema(description = "참여 상태", example = "WAITING")
  private ParticipationStatus status;
}
