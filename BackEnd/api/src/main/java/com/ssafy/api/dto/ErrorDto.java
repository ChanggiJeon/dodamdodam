package com.ssafy.api.dto;

import com.ssafy.api.exception.CustomErrorCode;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;

@ApiModel("Error")
public class ErrorDto {

    @Builder
    @AllArgsConstructor
    public static class ErrorResponse {

        private CustomErrorCode customErrorCode;
        private String detailMessage;
    }
}
