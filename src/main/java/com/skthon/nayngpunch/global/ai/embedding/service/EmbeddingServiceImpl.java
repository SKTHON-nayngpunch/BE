/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.global.ai.embedding.service;

import java.util.List;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.stereotype.Service;

import com.skthon.nayngpunch.global.ai.exception.EmbeddingErrorCode;
import com.skthon.nayngpunch.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmbeddingServiceImpl implements EmbeddingService {

  private final EmbeddingModel embeddingModel;

  @Override
  public float[] embed(String text) {
    if (text == null) throw new CustomException(EmbeddingErrorCode.EMBEDDING_ILLEGAL_ARGUMENT);
    EmbeddingResponse resp = embeddingModel.embedForResponse(List.of(text));
    return resp.getResults().getFirst().getOutput();
  }
}
