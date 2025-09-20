/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.food.mapper;

import com.skthon.nayngpunch.domain.food.dto.request.FoodCreateRequest;
import com.skthon.nayngpunch.domain.food.dto.response.FoodResponse;
import com.skthon.nayngpunch.domain.food.entity.Food;
import com.skthon.nayngpunch.domain.user.entity.User;

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
  public static FoodResponse.FoodCreateResponse toFoodCreateResponse(Food food) {
    return FoodResponse.FoodCreateResponse.builder()
        .foodId(food.getId())
        .title(food.getTitle())
        .maxMember(food.getMaxMember())
        .content(food.getContent())
        .foodImageUrl(food.getFoodImageUrl())
        .name(food.getName())
        .conditionScore(food.getConditionScore())
        .analysis(food.getAnalysis())
        .build();
  }

  // Request + User -> Entity
  public static Food toFood(FoodCreateRequest request, User user) {
    return Food.builder()
        .user(user)
        .foodImageUrl(request.getFoodImageUrl())
        .name(request.getName())
        .conditionScore(request.getConditionScore())
        .analysis(request.getAnalysis())
        .title(request.getTitle())
        .content(request.getContent())
        .maxMember(request.getMaxMember())
        // totalCount, outCount, status 는 서비스에서 정책적으로 채움
        .build();
  }
}
