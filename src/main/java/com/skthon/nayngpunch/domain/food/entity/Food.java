/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.food.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.skthon.nayngpunch.domain.user.entity.User;
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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "food")
public class Food extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "food_id", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계
  @JoinColumn(name = "user_id", nullable = false) // FK 컬럼명
  private User user; // 사용자 아이디

  @Column(name = "title", nullable = true)
  private String title; // 제목

  @Column(name = "name", nullable = true)
  private String name; // 음식명

  @Column(name = "content", nullable = false, columnDefinition = "TEXT")
  private String content; // 설명

  @Column(name = "start_date", nullable = false)
  private LocalDate startDate; // 모집 시작일 (끝나는 날짜는 모집 시작일 기준 3일)

  @Column(name = "max_member", nullable = false)
  private Integer maxMember; // 최대 모집 멤버

  @Column(name = "total_count", nullable = false)
  private Integer totalCount; // 총 개수

  @Column(name = "condition_score", nullable = true)
  private Integer conditionScore; // 신선도 / 품질 점수

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private FoodStatus status;

  @Column(name = "out_count", nullable = false)
  private Integer outCount; // 최대 모집 멤버

  @Column(name = "analysis", nullable = false, columnDefinition = "TEXT")
  private String analysis; // 설명
}
