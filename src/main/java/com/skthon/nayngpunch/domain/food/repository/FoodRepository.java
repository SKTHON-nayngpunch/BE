/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.domain.food.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.skthon.nayngpunch.domain.food.entity.Food;

public interface FoodRepository extends JpaRepository<Food, Long> {

  interface FoodUrgentProjection {
    Long getFoodId();

    String getFoodImageUrl();

    java.sql.Date getCreatedDate();

    String getTitle();

    String getLocation();

    String getName();

    Integer getTotalCount();

    Integer getConditionScore();

    Integer getMaxMember();

    Integer getCurrentMember();

    Long getRemainingSeconds();
  }

  @Query(
      value =
          """
    SELECT
      f.food_id                          AS foodId,
      f.food_image_url                   AS foodImageUrl,
      DATE(f.created_at)                 AS createdDate,
      f.title                            AS title,
      u.address                          AS location,
      f.name                             AS name,
      f.total_count                      AS totalCount,
      f.condition_score                  AS conditionScore,
      f.max_member                       AS maxMember,
      f.out_count                        AS currentMember,
      TIMESTAMPDIFF(
        SECOND,
        NOW(),
        DATE_ADD(f.created_at, INTERVAL 3 DAY)
      )                                   AS remainingSeconds
--    , (6371 * ACOS(   -- ✅ 거리 계산 (단위: km)
--        COS(RADIANS(:lat))
--        * COS(RADIANS(u.latitude))
--        * COS(RADIANS(u.longitude) - RADIANS(:lon))
--        + SIN(RADIANS(:lat))
--        * SIN(RADIANS(u.latitude))
--    )) AS distance
    FROM food f
    JOIN users u ON u.id = f.user_id
    WHERE f.status = 'WAITING'
      AND DATE_ADD(f.created_at, INTERVAL 3 DAY) > NOW()
    HAVING
      (
        (remainingSeconds BETWEEN 0 AND 86400)
        OR (maxMember - currentMember) <= 2
      )
--      AND distance <= 1   -- ✅ 반경 1km 조건
    ORDER BY remainingSeconds ASC
    """,
      nativeQuery = true)
  List<FoodUrgentProjection> findClosingSoon(
      /**
       * @Param("lat") double lat, -- 현재 사용자 위도 -- @Param("lon") double lon -- 현재 사용자 경도 *
       */
      );

  interface FoodDetailProjection {
    Long getFoodId();

    String getFoodImageUrl();

    java.sql.Date getCreatedDate();

    String getTitle();

    String getWriterProfileImageUrl();

    String getWriterNickname();

    String getWriterAddress();

    Float getWriterFreshScore();

    String getName();

    Integer getTotalCount();

    Integer getConditionScore();

    Long getRemainingSeconds();

    String getAnalysis();

    String getContent();

    Integer getMaxMember();

    Integer getCurrentMember();
  }

  @Query(
      value =
          """
      SELECT
        f.food_id                                            AS foodId,
        f.food_image_url                                     AS foodImageUrl,
        DATE(f.created_at)                                   AS createdDate,
        f.title                                              AS title,
        u.profile_image_url                                  AS writerProfileImageUrl,
        u.nickname                                           AS writerNickname,
        u.address                                            AS writerAddress,
        u.fresh_score                                        AS writerFreshScore,
        f.name                                               AS name,
        f.total_count                                        AS totalCount,
        f.condition_score                                    AS conditionScore,
        GREATEST(
          TIMESTAMPDIFF(SECOND, NOW(), DATE_ADD(f.created_at, INTERVAL 3 DAY)),
          0
        )                                                    AS remainingSeconds,
        f.analysis                                           AS analysis,
        f.content                                            AS content,
        f.max_member                                         AS maxMember,
        f.out_count                                          AS currentMember
      FROM food f
      JOIN users u ON u.id = f.user_id
      WHERE f.food_id = :foodId
      """,
      nativeQuery = true)
  Optional<FoodDetailProjection> findDetailById(Long foodId);

  //
  //  interface FoodUrgentSearchProjection {
  //    Long getFoodId();
  //
  //    String getFoodImageUrl();
  //
  //    java.sql.Date getCreatedDate();
  //
  //    String getTitle();
  //
  //    String getLocation();
  //
  //    String getName();
  //
  //    Integer getTotalCount();
  //
  //    Integer getConditionScore();
  //
  //    Integer getMaxMember();
  //
  //    Integer getCurrentMember();
  //
  //    Long getRemainingSeconds();
  //  }
  //
  //  @Query(
  //      value =
  //          """
  //      SELECT
  //        f.food_id                          AS foodId,
  //        f.food_image_url                   AS foodImageUrl,
  //        DATE(f.created_at)                 AS createdDate,
  //        f.title                            AS title,
  //        u.address                          AS location,
  //        f.name                             AS name,
  //        f.total_count                      AS totalCount,
  //        f.condition_score                  AS conditionScore,
  //        f.max_member                       AS maxMember,
  //        f.out_count                        AS currentMember,
  //        TIMESTAMPDIFF(
  //          SECOND,
  //          NOW(),
  //          DATE_ADD(f.created_at, INTERVAL 3 DAY)
  //        )                                   AS remainingSeconds
  //      FROM food f
  //      JOIN users u ON u.id = f.user_id
  //      WHERE f.status = 'WAITING'
  //        AND DATE_ADD(f.created_at, INTERVAL 3 DAY) > NOW()           -- 만료 제외
  //        AND (
  //              f.name  LIKE CONCAT('%', :keyword, '%')
  //           OR f.title LIKE CONCAT('%', :keyword, '%')
  //           OR f.content LIKE CONCAT('%', :keyword, '%')
  //        )
  //      HAVING
  //          (remainingSeconds BETWEEN 0 AND 86400)                     -- 마감 24시간 이내
  //       OR (maxMember - currentMember) <= 2                           -- 또는 남은 슬롯 ≤ 2
  //      ORDER BY remainingSeconds ASC
  //      """,
  //      nativeQuery = true)
  //  List<FoodUrgentProjection> searchClosingSoonByKeyword(String keyword);
  interface FoodListProjection {
    Long getFoodId();

    String getFoodImageUrl();

    java.sql.Date getCreatedDate();

    String getTitle();

    String getLocation();

    String getName();

    Integer getTotalCount();

    Integer getConditionScore();

    Integer getMaxMember();

    Integer getCurrentMember();

    Long getRemainingSeconds();
  }

  // 마감임박순
  @Query(
      value =
          """
      SELECT
        f.food_id AS foodId,
        f.food_image_url AS foodImageUrl,
        DATE(f.created_at) AS createdDate,
        f.title AS title,
        u.address AS location,
        f.name AS name,
        f.total_count AS totalCount,
        f.condition_score AS conditionScore,
        f.max_member AS maxMember,
        (
          SELECT COUNT(*)
          FROM participation p
          WHERE p.food_id = f.food_id
          AND p.status IN ('WAITING','COMPLETE')
        ) AS currentMember,
        TIMESTAMPDIFF(
          SECOND, NOW(), DATE_ADD(f.created_at, INTERVAL 3 DAY)
        ) AS remainingSeconds
      FROM food f
      JOIN users u ON u.id = f.user_id
      WHERE f.status = 'WAITING'
        AND f.name LIKE %:keyword%
      ORDER BY remainingSeconds ASC
      """,
      nativeQuery = true)
  List<FoodListProjection> findByKeywordOrderByDeadline(String keyword);

  // 신선도순
  @Query(
      value =
          """
      SELECT
        f.food_id AS foodId,
        f.food_image_url AS foodImageUrl,
        DATE(f.created_at) AS createdDate,
        f.title AS title,
        u.address AS location,
        f.name AS name,
        f.total_count AS totalCount,
        f.condition_score AS conditionScore,
        f.max_member AS maxMember,
        (
          SELECT COUNT(*)
          FROM participation p
          WHERE p.food_id = f.food_id
          AND p.status IN ('WAITING','COMPLETE')
        ) AS currentMember,
        TIMESTAMPDIFF(
          SECOND, NOW(), DATE_ADD(f.created_at, INTERVAL 3 DAY)
        ) AS remainingSeconds
      FROM food f
      JOIN users u ON u.id = f.user_id
      WHERE f.status = 'WAITING'
        AND f.name LIKE %:keyword%
      ORDER BY conditionScore DESC
      """,
      nativeQuery = true)
  List<FoodListProjection> findByKeywordOrderByFreshness(String keyword);
}
