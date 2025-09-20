package com.skthon.nayngpunch.domain.participation.entity;


import com.skthon.nayngpunch.domain.goods.entity.Food;
import com.skthon.nayngpunch.domain.user.entity.User;
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
@Table(name = "participation")
public class Participation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "participation_id", nullable = false)
  private Long id; // 참여 아이디

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user; // 사용자 아이디 (FK)

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "food_id", nullable = false)
  private Food food; // 음식 아이디 (FK)

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private ParticipationStatus status; // 상태 (WAITING, COMPLETE, FAIL)
}
