package com.ssafy.core.dto;

import com.ssafy.core.exception.CustomErrorCode;
import com.ssafy.core.exception.CustomException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Builder
@RequiredArgsConstructor
@Getter
@Schema(name = "Error")
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

    public static ResponseEntity<ErrorResponse> toResponseEntity(CustomException customException) {
        return ResponseEntity
                .status(customException.getCustomErrorCode().getHttpStatus())
                .body(ErrorResponse.builder()
                        .customErrorCode(customException.getCustomErrorCode())
                        .status(customException.getCustomErrorCode().getHttpStatus().value())
                        .detailMessage(customException.getDetailMessage())
                        .build()
                );
    }
}
