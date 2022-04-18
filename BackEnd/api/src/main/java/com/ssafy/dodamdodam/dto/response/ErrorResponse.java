package com.ssafy.dodamdodam.dto.response;

import com.ssafy.dodamdodam.exception.CustomErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class ErrorResponse {

    private CustomErrorCode customErrorCode;
    private String detailMessage;
}
