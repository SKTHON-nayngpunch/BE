/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.goods.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skthon.nayngpunch.domain.goods.entity.Food;

public interface FoodRepository extends JpaRepository<Food, Long> {}
