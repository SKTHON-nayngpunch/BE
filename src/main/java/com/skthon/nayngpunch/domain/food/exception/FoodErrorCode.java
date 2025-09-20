/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.food.exception;

import org.springframework.http.HttpStatus;

import com.skthon.nayngpunch.global.exception.model.BaseErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FoodErrorCode implements BaseErrorCode {
  FRESHNESS_TOO_LOW("FOOD_4041", "신선도 7 미만은 나눔 등록이 불가합니다.", HttpStatus.BAD_REQUEST);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
