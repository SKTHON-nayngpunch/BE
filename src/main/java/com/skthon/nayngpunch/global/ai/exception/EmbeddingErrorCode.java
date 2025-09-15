package com.skthon.nayngpunch.global.ai.exception;

import com.skthon.nayngpunch.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum EmbeddingErrorCode implements BaseErrorCode {
    EMBEDDING_ILLEGAL_ARGUMENT("EMBEDDING_4001", "유효하지 않은 임베딩 요청 값입니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
