/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.food.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skthon.nayngpunch.domain.food.dto.request.FoodCreateRequest;
import com.skthon.nayngpunch.domain.food.dto.response.FoodAnalysisResponse;
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
import com.skthon.nayngpunch.global.s3.entity.PathName;
import com.skthon.nayngpunch.global.s3.service.S3Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FoodService {

  private final FoodRepository foodRepository;
  private final UserService userService;
  private final FoodMapper foodMapper;
  private final S3Service s3Service;

  @Value("${spring.ai.openai.api-key}")
  private String openaiApiKey;

  @Transactional(readOnly = true)
  public FoodAnalysisResponse getFoodAnalysis(MultipartFile foodImage) {
    try {
      // 1. Base64 인코딩
      String base64Image = Base64.getEncoder().encodeToString(foodImage.getBytes());
      String mimeType = foodImage.getContentType(); // 예: image/jpeg
      String dataUri = "data:" + mimeType + ";base64," + base64Image;

      // 2. 프롬프트
      String prompt =
          """
당신은 이미지 속 '식재료'를 판별하는 심사원입니다. 아래 규칙을 모두 지키고 한국어로만 답하세요.

[출력 형식 — 정확히 3줄]
1줄: 식재료 이름만 한 단어로.
2줄: 신선도 점수 정수만 (1~10).
3줄: 신선도 판단 이유 60~120자, 줄바꿈 없이 한 문단.
""";

      // 3. JSON 객체 구성 (Map → JSON)
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object> payload =
          Map.of(
              "model",
              "gpt-4o",
              "messages",
              List.of(
                  Map.of(
                      "role",
                      "user",
                      "content",
                      List.of(
                          Map.of("type", "text", "text", prompt),
                          Map.of("type", "image_url", "image_url", Map.of("url", dataUri))))));

      String requestJson = mapper.writeValueAsString(payload);

      // 4. OpenAI API 호출
      OkHttpClient client = new OkHttpClient();
      RequestBody body =
          RequestBody.create(
              requestJson.getBytes(StandardCharsets.UTF_8),
              MediaType.get("application/json; charset=utf-8"));

      Request request =
          new Request.Builder()
              .url("https://api.openai.com/v1/chat/completions")
              .addHeader("Authorization", "Bearer " + openaiApiKey)
              .post(body)
              .build();

      try (Response response = client.newCall(request).execute()) {
        String responseBody = response.body() != null ? response.body().string() : "";
        if (!response.isSuccessful()) {
          throw new RuntimeException("OpenAI API 호출 실패: " + response.code() + " - " + responseBody);
        }

        // 5. JSON 파싱
        JsonNode root = mapper.readTree(responseBody);
        String content = root.path("choices").get(0).path("message").path("content").asText();

        // 6. 줄바꿈 분리
        String[] lines = content.split("\\r?\\n");
        String name = lines.length > 0 ? lines[0].trim() : "미확인";
        int score = 0;
        try {
          if (lines.length > 1) score = Integer.parseInt(lines[1].trim());
        } catch (NumberFormatException ignored) {
        }
        String reason = lines.length > 2 ? lines[2].trim() : "";

        // 7. DTO 반환
        return foodMapper.toFoodAnalysisResponse(name, score, reason);
      }

    } catch (Exception e) {
      throw new RuntimeException("이미지 분석 중 오류 발생", e);
    }
  }

  @Transactional
  public FoodResponse.FoodCreateResponse createFood(
      MultipartFile foodImage, FoodCreateRequest foodCreateRequest) {
    // 정책: 7 미만이면 저장 거부
    if (foodCreateRequest.getConditionScore() < 7) {
      throw new CustomException(FoodErrorCode.FRESHNESS_TOO_LOW);
    }

    User writer = userService.getCurrentUser();

    String imageUrl = null;
    if (foodImage != null) {
      imageUrl = s3Service.uploadFile(PathName.FOOD, foodImage);
    }

    Food saved =
        foodRepository.save(
            Food.builder()
                .user(writer)
                .foodImageUrl(imageUrl)
                .name(foodCreateRequest.getName())
                .conditionScore(foodCreateRequest.getConditionScore())
                .analysis(foodCreateRequest.getAnalysis())
                .title(foodCreateRequest.getTitle())
                .maxMember(foodCreateRequest.getMaxMember())
                .content(foodCreateRequest.getContent())
                .status(FoodStatus.WAITING)
                .totalCount(foodCreateRequest.getMaxMember())
                .outCount(0)
                .build());

    return FoodMapper.toFoodCreateResponse(imageUrl, saved);
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

  //  @Transactional(readOnly = true)
  //  public List<FoodUrgentItemResponse> searchClosingSoon(String keyword) {
  //    // 널/공백 방어
  //    String q = (keyword == null) ? "" : keyword.trim();
  //
  //    return foodRepository.searchClosingSoonByKeyword(q).stream()
  //        .map(
  //            p ->
  //                FoodUrgentItemResponse.builder()
  //                    .foodId(p.getFoodId())
  //                    .foodImageUrl(p.getFoodImageUrl())
  //                    .createdDate(p.getCreatedDate().toLocalDate())
  //                    .title(p.getTitle())
  //                    .location(p.getLocation())
  //                    .name(p.getName())
  //                    .totalCount(p.getTotalCount())
  //                    .conditionScore(p.getConditionScore())
  //                    .maxMember(p.getMaxMember())
  //                    .currentMember(p.getCurrentMember())
  //                    .remainingSeconds(p.getRemainingSeconds())
  //                    .build())
  //        .toList();
  //  }
  @Transactional(readOnly = true)
  public List<FoodUrgentItemResponse> searchFoods(String keyword, String sort) {
    List<FoodRepository.FoodListProjection> results;

    switch (sort.toLowerCase()) {
      case "freshness":
        results = foodRepository.findByKeywordOrderByFreshness(keyword);
        break;
      case "deadline":
      default:
        results = foodRepository.findByKeywordOrderByDeadline(keyword);
        break;
    }

    return results.stream()
        .map(
            r ->
                FoodUrgentItemResponse.builder()
                    .foodId(r.getFoodId())
                    .foodImageUrl(r.getFoodImageUrl())
                    .createdDate(r.getCreatedDate().toLocalDate())
                    .title(r.getTitle())
                    .location(r.getLocation())
                    .name(r.getName())
                    .totalCount(r.getTotalCount())
                    .conditionScore(r.getConditionScore())
                    .maxMember(r.getMaxMember())
                    .currentMember(r.getCurrentMember())
                    .remainingSeconds(r.getRemainingSeconds())
                    .build())
        .toList();
  }
}
