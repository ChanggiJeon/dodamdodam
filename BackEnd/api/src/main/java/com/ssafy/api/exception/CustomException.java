package com.ssafy.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private CustomErrorCode customErrorCode;
    private String detailMessage;

    public CustomException(CustomErrorCode customErrorCode) {
        this.customErrorCode = customErrorCode;
        this.detailMessage = customErrorCode.getMessage();
    }

}
