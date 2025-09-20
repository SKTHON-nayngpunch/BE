/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.participation.mapper;

import org.springframework.stereotype.Component;

import com.skthon.nayngpunch.domain.participation.dto.response.CreateParticipateResponse;
import com.skthon.nayngpunch.domain.participation.dto.response.ShareResultResponse;
import com.skthon.nayngpunch.domain.participation.entity.Participation;
import com.skthon.nayngpunch.domain.user.entity.User;

@Component
public class ParticipatationMapper {

  public CreateParticipateResponse toCreateParicipateResponse(Participation participation) {
    return CreateParticipateResponse.builder()
        .participatationId(participation.getId())
        .userId(participation.getUser().getId())
        .foodId(participation.getFood().getId())
        .status(participation.getStatus())
        .build();
  }

  public ShareResultResponse toShareResultResponse(
      Participation participation, User user, Float addCarbonCount) {
    return ShareResultResponse.builder()
        .participateId(participation.getId())
        .userId(user.getId())
        .nickname(user.getNickname())
        .carbonCount(addCarbonCount)
        .totalCarbonCount(user.getCarbonCount())
        .build();
  }
}
