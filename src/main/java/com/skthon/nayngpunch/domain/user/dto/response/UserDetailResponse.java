package com.skthon.nayngpunch.domain.user.dto.response;

import com.skthon.nayngpunch.domain.user.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "UserDetailResponse DTO", description = "사용자 정보 응답 반환")
public class UserDetailResponse {

    @Schema(description = "사용자 식별자", example = "1")
    private Long userId;

    @Schema(description = "아이디(이메일)", example = "example@example.com")
    private String username;

    @Schema(description = "닉네임", example = "나나나난")
    private String nickname;

    @Schema(
        description = "프로필 이미지 URL",
        example = "http://k.kakaocdn.net/dn/oOPCG/btsPjlOHjk6/6jx0PyBKkHHyCfbV8IY741/img_640x640.jpg")
    private String profileImageUrl;

    @Schema(description = "역할/권한", example = "ROLE_USER")
    private Role role;
}
