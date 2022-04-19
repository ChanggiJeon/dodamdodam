package com.ssafy.api.exception;

import com.ssafy.api.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

import static com.ssafy.api.exception.CustomErrorCode.INTERVAL_SERVER_ERROR;
import static com.ssafy.api.exception.CustomErrorCode.INVALID_REQUEST;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e, HttpServletRequest request) {
        log.error("errorCode: {}, url: {}, message: {}", e.getCustomErrorCode(), request.getRequestURI(), e.getDetailMessage());

        return ErrorResponse.toResponseEntity(e.getCustomErrorCode());
    }

    @ExceptionHandler(value = {
            HttpRequestMethodNotSupportedException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ErrorResponse> handleRestException(Exception e, HttpServletRequest request) {
        log.error("url: {}, message: {}", request.getRequestURI(), e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .customErrorCode(INVALID_REQUEST)
                .detailMessage(INVALID_REQUEST.getMessage())
                .build();

        return ErrorResponse.toResponseEntity(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        log.error("url: {}, message: {}", request.getRequestURI(), e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .customErrorCode(INTERVAL_SERVER_ERROR)
                .detailMessage(INTERVAL_SERVER_ERROR.getMessage())
                .build();

        return ErrorResponse.toResponseEntity(errorResponse);
    }
}
