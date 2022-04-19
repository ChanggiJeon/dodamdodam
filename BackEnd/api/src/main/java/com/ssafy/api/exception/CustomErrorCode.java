package com.ssafy.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomErrorCode {
    NO_SUCH_USER("해당하는 유저가 없습니다."),
    DUPLICATE_USER_ID("이미 사용중인 아이디입니다."),
    INTERVAL_SERVER_ERROR("서버에 오류가 발생했습니다."),
    INVALID_REQUEST("잘못된 요청입니다.");

    private String message;

}
