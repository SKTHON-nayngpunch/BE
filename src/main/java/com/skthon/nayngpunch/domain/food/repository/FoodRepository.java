/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.food.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skthon.nayngpunch.domain.food.entity.Food;

public interface FoodRepository extends JpaRepository<Food, Long> {}
