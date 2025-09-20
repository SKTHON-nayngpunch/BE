/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.goods.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Food", description = "음식 관리 api")
public class FoodController {}
