/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.participation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skthon.nayngpunch.domain.food.entity.Food;
import com.skthon.nayngpunch.domain.food.repository.FoodRepository;
import com.skthon.nayngpunch.domain.participation.dto.response.CreateParticipateResponse;
import com.skthon.nayngpunch.domain.participation.dto.response.ShareResultResponse;
import com.skthon.nayngpunch.domain.participation.entity.Participation;
import com.skthon.nayngpunch.domain.participation.entity.ParticipationStatus;
import com.skthon.nayngpunch.domain.participation.mapper.ParticipatationMapper;
import com.skthon.nayngpunch.domain.participation.repository.ParticipationRepository;
import com.skthon.nayngpunch.domain.user.entity.User;
import com.skthon.nayngpunch.domain.user.repository.UserRepository;
import com.skthon.nayngpunch.domain.user.service.UserService;
import com.skthon.nayngpunch.global.ai.prompt.service.PromptService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParticipationServiceImpl implements ParticipationService {

  private final ParticipationRepository participationRepository;
  private final UserService userService;
  private final FoodRepository foodRepository;
  private final ParticipatationMapper participationMapper;
  private final PromptService promptService;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public CreateParticipateResponse createParticipate(Long foodId) {
    Food food = foodRepository.findById(foodId).orElseThrow();

    Participation participation =
        Participation.builder().user(userService.getCurrentUser()).food(food).build();
    participationRepository.save(participation);
    return participationMapper.toCreateParicipateResponse(participation);
  }

  @Override
  public ShareResultResponse updateSubComplete(Long participationId) {
    Participation participation = participationRepository.findById(participationId).orElse(null);

    Food food = participation.getFood();
    User user = userService.getCurrentUser();

    String prompt = food.getName() + "1개를 쓰레기로 버렸을 때, kg당 탄소 배출량의 중간값을 설명, 단위는 전부 빼고 숫자만 반환해줘";
    Float addCarbonCount = Float.parseFloat(promptService.ask(prompt));
    user.updateShareResult(false, addCarbonCount);
    participation.updateStatus(ParticipationStatus.SUB_COMPLETE);
    userRepository.save(user);

    return participationMapper.toShareResultResponse(participation, user, addCarbonCount);
  }

  @Override
  public ShareResultResponse updateComplete(Long participationId) {
    Participation participation = participationRepository.findById(participationId).orElse(null);

    Food food = participation.getFood();
    User user = participation.getUser();

    String prompt = food.getName() + "1개를 쓰레기로 버렸을 때, kg당 탄소 배출량의 중간값을 설명, 단위는 전부 빼고 숫자만 반환해줘";
    Float addCarbonCount = Float.parseFloat(promptService.ask(prompt));
    user.updateShareResult(true, addCarbonCount);
    participation.updateStatus(ParticipationStatus.COMPLETE);
    userRepository.save(user);

    return participationMapper.toShareResultResponse(participation, user, addCarbonCount);
  }
}
