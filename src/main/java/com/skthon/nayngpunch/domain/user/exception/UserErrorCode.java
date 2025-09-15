package com.skthon.nayngpunch.domain.user.exception;

import com.skthon.nayngpunch.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements BaseErrorCode {
    USERNAME_ALREADY_EXISTS("USER_4001", "이미 존재하는 사용자 아이디입니다.", HttpStatus.BAD_REQUEST),
    CODE_ALREADY_EXISTS("USER_4002", "이미 존재하는 사용자 코드입니다.", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("USER_4003", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    UNAUTHORIZED("USER_4004", "인증되지 않은 사용자입니다.", HttpStatus.UNAUTHORIZED),
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;
}
