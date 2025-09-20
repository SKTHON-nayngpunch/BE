/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.food.mapper;

import org.springframework.stereotype.Component;

import com.skthon.nayngpunch.domain.food.dto.response.FoodAnalysisResponse;
import com.skthon.nayngpunch.domain.food.dto.response.FoodResponse;
import com.skthon.nayngpunch.domain.food.entity.Food;

@Component
public class FoodMapper {

  //  // Entity -> Response
  //  public static FoodResponse toFoodResponse(Food food) {
  //    return FoodResponse.builder()
  //        .id(food.getId())
  //        .title(food.getTitle())
  //        .maxMember(food.getMaxMember())
  //        .content(food.getContent())
  //        .status(food.getStatus())
  //        .totalCount(food.getTotalCount())
  //        .outCount(food.getOutCount())
  //        .conditionScore(food.getConditionScore())
  //        .createdAt(food.getCreatedAt())
  //        .build();
  //  }

  // Entity -> FoodCreateResponse
  public static FoodResponse.FoodCreateResponse toFoodCreateResponse(String imageUrl, Food food) {
    return FoodResponse.FoodCreateResponse.builder()
        .foodId(food.getId())
        .title(food.getTitle())
        .maxMember(food.getMaxMember())
        .content(food.getContent())
        .foodImageUrl(imageUrl)
        .name(food.getName())
        .conditionScore(food.getConditionScore())
        .analysis(food.getAnalysis())
        .build();
  }

  public static FoodAnalysisResponse toFoodAnalysisResponse(
      String name, Integer conditionScore, String analysis) {
    return FoodAnalysisResponse.builder()
        .name(name)
        .conditionScore(conditionScore)
        .analysis(analysis)
        .build();
  }
}
