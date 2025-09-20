/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.participation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skthon.nayngpunch.domain.participation.entity.Participation;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
  List<Participation> findAllByUserId(Long userId); // 참여자가 나

  List<Participation> findAllByFoodIdIn(List<Long> foodIds); // 음식 작성자가 나
}
