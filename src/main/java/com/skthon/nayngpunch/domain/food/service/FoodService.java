/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.food.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skthon.nayngpunch.domain.food.dto.request.FoodCreateRequest;
import com.skthon.nayngpunch.domain.food.dto.response.FoodDetailResponse;
import com.skthon.nayngpunch.domain.food.dto.response.FoodResponse;
import com.skthon.nayngpunch.domain.food.dto.response.FoodUrgentItemResponse;
import com.skthon.nayngpunch.domain.food.entity.Food;
import com.skthon.nayngpunch.domain.food.entity.FoodStatus;
import com.skthon.nayngpunch.domain.food.exception.FoodErrorCode;
import com.skthon.nayngpunch.domain.food.mapper.FoodMapper;
import com.skthon.nayngpunch.domain.food.repository.FoodRepository;
import com.skthon.nayngpunch.domain.user.entity.User;
import com.skthon.nayngpunch.domain.user.service.UserService;
import com.skthon.nayngpunch.global.exception.CustomException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FoodService {

  private final FoodRepository foodRepository;
  private final UserService userService;

  @Transactional
  public FoodResponse.FoodCreateResponse createFood(FoodCreateRequest req) {
    // 정책: 7 미만이면 저장 거부
    if (req.getConditionScore() < 7) {
      throw new CustomException(FoodErrorCode.FRESHNESS_TOO_LOW);
    }

    User writer = userService.getCurrentUser();

    Food saved =
        foodRepository.save(
            Food.builder()
                .user(writer)
                .foodImageUrl(req.getFoodImageUrl())
                .name(req.getName())
                .conditionScore(req.getConditionScore())
                .analysis(req.getAnalysis())
                .title(req.getTitle())
                .maxMember(req.getMaxMember())
                .content(req.getContent())
                .status(FoodStatus.WAITING)
                .totalCount(req.getMaxMember())
                .outCount(0)
                .build());

    return FoodMapper.toFoodCreateResponse(saved);
  }

  @Transactional(readOnly = true)
  public List<FoodUrgentItemResponse> getClosingSoonList() {
    return foodRepository.findClosingSoon().stream()
        .map(
            p ->
                FoodUrgentItemResponse.builder()
                    .foodId(p.getFoodId())
                    .foodImageUrl(p.getFoodImageUrl())
                    .createdDate(p.getCreatedDate().toLocalDate())
                    .title(p.getTitle())
                    .location(p.getLocation())
                    .name(p.getName())
                    .totalCount(p.getTotalCount())
                    .conditionScore(p.getConditionScore())
                    .maxMember(p.getMaxMember())
                    .currentMember(p.getCurrentMember())
                    .remainingSeconds(p.getRemainingSeconds())
                    .build())
        .toList();
  }

  @Transactional(readOnly = true)
  public FoodDetailResponse getDetail(Long foodId) {
    var p =
        foodRepository
            .findDetailById(foodId)
            .orElseThrow(() -> new CustomException(FoodErrorCode.FOOD_NOT_FOUND));

    return FoodDetailResponse.builder()
        .foodId(p.getFoodId())
        .foodImageUrl(p.getFoodImageUrl())
        .createdDate(p.getCreatedDate().toLocalDate())
        .title(p.getTitle())
        .writerProfileImageUrl(p.getWriterProfileImageUrl())
        .writerNickname(p.getWriterNickname())
        .writerAddress(p.getWriterAddress())
        .writerFreshScore(p.getWriterFreshScore())
        .name(p.getName())
        .totalCount(p.getTotalCount())
        .conditionScore(p.getConditionScore())
        .remainingSeconds(p.getRemainingSeconds())
        .analysis(p.getAnalysis())
        .content(p.getContent())
        .maxMember(p.getMaxMember())
        .currentMember(p.getCurrentMember()) // = out_count
        .build();
  }
}
