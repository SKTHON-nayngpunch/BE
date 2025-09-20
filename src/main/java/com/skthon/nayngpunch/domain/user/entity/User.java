/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.user.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skthon.nayngpunch.domain.food.entity.Food;
import com.skthon.nayngpunch.domain.participation.entity.Participation;
import com.skthon.nayngpunch.global.common.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @JsonIgnore
  @Column(name = "password")
  private String password;

  @Column(name = "nickname", nullable = false)
  private String nickname;

  @Column(name = "profile_image_url", nullable = false)
  private String profileImageUrl;

  @Column(name = "address")
  private String address;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "chance_count", nullable = false)
  @Builder.Default
  private Long chanceCount = 5L;

  @Column(name = "fresh_score", nullable = false)
  @Builder.Default
  private Float freshScore = 0.0f;

  @Column(name = "share_count", nullable = false)
  @Builder.Default
  private Long shareCount = 0L;

  @Column(name = "receive_count", nullable = false)
  @Builder.Default
  private Long receiveCount = 0L;

  @Column(name = "carbon_count", nullable = false)
  @Builder.Default
  private Float carbonCount = 0.00f;

  @Column(name = "latitude")
  private Float latitude;

  @Column(name = "longitude")
  private Float longitude;

  @Column(name = "role", nullable = false)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private Role role = Role.ROLE_USER;

  @Column(name = "is_deleted", nullable = false)
  @Builder.Default
  private boolean isDeleted = false;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @Builder.Default
  private List<Food> foods = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @Builder.Default
  private List<Participation> participations = new ArrayList<>();

  public static User fromOAuth(String email, String nickname, String profileImageUrl) {
    return User.builder()
        .username(email)
        .password(UUID.randomUUID().toString())
        .nickname(nickname)
        .profileImageUrl(profileImageUrl)
        .role(Role.ROLE_USER)
        .build();
  }

  public void updateProfileImageUrl(String profileImageUrl) {
    this.profileImageUrl = profileImageUrl;
  }

  public void updateProfile(
      String nickname, String phoneNumber, String address, Float latitude, Float longitude) {
    this.nickname = nickname;
    this.phoneNumber = phoneNumber;
    this.address = address;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public void updateShareResult(Boolean isParticipation, Float addCarbonCount) {
    this.carbonCount += addCarbonCount;
    if (isParticipation) {
      chanceCount = chanceCount - 1;
      receiveCount = receiveCount + 1;
    } else {
      chanceCount = chanceCount + 1;
      shareCount = shareCount + 1;
    }
  }
}
