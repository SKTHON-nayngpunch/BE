/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.participation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "나눔 채팅 리스트 응답")
public class ShareListResponse {
  @Schema(description = "참여 식별자", example = "1")
  private Long participateId;

  @Schema(description = "사용자 식별자", example = "1")
  private Long userId;

  @Schema(description = "사용자 닉네임", example = "1")
  private String nickname;
}
