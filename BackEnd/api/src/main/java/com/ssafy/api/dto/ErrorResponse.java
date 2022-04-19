package com.ssafy.api.dto;

import com.ssafy.api.exception.CustomErrorCode;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Builder
@RequiredArgsConstructor
@Getter
@ApiModel("Error")
public class ErrorResponse {

    private final int status;
    private final LocalDateTime localDateTime = LocalDateTime.now();
    private final CustomErrorCode customErrorCode;
    private final String detailMessage;

    public static ResponseEntity<ErrorResponse> toResponseEntity(CustomErrorCode customErrorCode) {
        return ResponseEntity
                .status(customErrorCode.getHttpStatus().value())
                .body(ErrorResponse.builder()
                        .customErrorCode(customErrorCode)
                        .status(customErrorCode.getHttpStatus().value())
                        .detailMessage(customErrorCode.getMessage())
                        .build()
                );
    }

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorResponse errorResponse) {
        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(ErrorResponse.builder()
                        .customErrorCode(errorResponse.getCustomErrorCode())
                        .detailMessage(errorResponse.getDetailMessage())
                        .build()
                );
    }
}
