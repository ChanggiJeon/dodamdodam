package com.ssafy.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum CustomErrorCode {
    NO_SUCH_USER(NOT_FOUND, "해당하는 유저가 없습니다."),
    DUPLICATE_USER_ID(CONFLICT, "이미 사용중인 아이디입니다."),
    INTERVAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다."),
    INVALID_REQUEST(BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_TOKEN(BAD_REQUEST, "유효하지 않은 토큰입니다."),
    NOT_BELONG_FAMILY(UNAUTHORIZED, "해당 그룹에 권한이 없습니다.");
    private final HttpStatus httpStatus;
    private final String message;

}
