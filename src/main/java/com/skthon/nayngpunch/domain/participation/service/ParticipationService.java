/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.participation.service;

import com.skthon.nayngpunch.domain.participation.dto.response.CreateParticipateResponse;
import com.skthon.nayngpunch.domain.participation.dto.response.ShareResultResponse;

public interface ParticipationService {

  CreateParticipateResponse createParticipate(Long foodId);

  ShareResultResponse updateSubComplete(Long participationId);

  ShareResultResponse updateComplete(Long participationId);
}
