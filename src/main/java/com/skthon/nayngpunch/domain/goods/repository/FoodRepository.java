package com.skthon.nayngpunch.domain.goods.repository;

import com.skthon.nayngpunch.domain.goods.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {

}
