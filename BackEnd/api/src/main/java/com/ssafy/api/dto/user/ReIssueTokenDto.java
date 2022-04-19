package com.ssafy.api.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ReIssueTokenDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "ReIssueToken Response")
    public static class Response {

        @ApiModelProperty(value = "jwt 토큰", required = true, example = "ex123am45ple")
        private String jwtToken;

        @ApiModelProperty(value = "refresh 토큰", required = true, example = "ex123am45ple")
        private String refreshToken;
    }
}
