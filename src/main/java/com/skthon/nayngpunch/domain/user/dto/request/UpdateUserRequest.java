/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "UpdateUserRequest DTO", description = "사용자 정보 변경을 위한 데이터 전송")
public class UpdateUserRequest {

  @NotBlank(message = "사용자 닉네임 항목은 필수입니다.")
  @Schema(description = "사용자 닉네임", example = "냥냥펀치")
  private String nickname;

  @NotBlank(message = "사용자 전화번호 항목은 필수입니다.")
  @Schema(description = "사용자 전화번호", example = "010-1234-5678")
  private String phoneNumber;

  @NotBlank(message = "사용자 주소 항목은 필수입니다.")
  @Schema(description = "사용자 주소", example = "서울시 성북구 정릉로 21가길 10")
  private String address;

  @Schema(description = "위도", example = "0.37")
  private Float latitude;

  @Schema(description = "위도", example = "1.34")
  private Float longitude;
}
