package com.ssafy.api.dto.user;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;


@ApiModel("ReIssueToken")
public class ReIssueTokenDto {

    @Builder
    @AllArgsConstructor
    public static class Response {
        private String jwtToken;
        private String refreshToken;
    }
}
