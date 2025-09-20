/* 
 * Copyright (c) LikeLion13th Problem not Found 
 */
package com.skthon.nayngpunch.global.ai.prompt.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class PromptService {

  private final ChatClient chatClient;

  public PromptService(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  public String ask(String prompt) {
    return chatClient.prompt().user(prompt).call().content(); // 모델 응답 텍스트만 추출
  }
}
