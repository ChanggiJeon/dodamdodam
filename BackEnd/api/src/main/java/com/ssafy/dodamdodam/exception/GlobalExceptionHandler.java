package com.ssafy.dodamdodam.exception;

import com.ssafy.dodamdodam.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

import static com.ssafy.dodamdodam.exception.CustomErrorCode.INTERVAL_SERVER_ERROR;
import static com.ssafy.dodamdodam.exception.CustomErrorCode.INVALID_REQUEST;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ErrorResponse handleCustomException(CustomException e, HttpServletRequest request){
        log.error("errorCode: {}, url: {}, message: {}", e.getCustomErrorCode(), request.getRequestURI(), e.getDetailMessage());
        return ErrorResponse.builder()
                .customErrorCode(e.getCustomErrorCode())
                .detailMessage(e.getDetailMessage())
                .build();
    }

    @ExceptionHandler(value = {
            HttpRequestMethodNotSupportedException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ErrorResponse handleRestException(Exception e, HttpServletRequest request){
        log.error("url: {}, message: {}", request.getRequestURI(), e.getMessage());
        return ErrorResponse.builder()
                .customErrorCode(INVALID_REQUEST)
                .detailMessage(INVALID_REQUEST.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e, HttpServletRequest request){
        log.error("url: {}, message: {}", request.getRequestURI(), e.getMessage());
        return ErrorResponse.builder()
                .customErrorCode(INTERVAL_SERVER_ERROR)
                .detailMessage(INTERVAL_SERVER_ERROR.getMessage())
                .build();
    }
}
