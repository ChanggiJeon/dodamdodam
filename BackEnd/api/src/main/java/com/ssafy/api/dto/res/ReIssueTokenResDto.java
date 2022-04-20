package com.ssafy.api.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "ReIssueToken Response")
public class ReIssueTokenResDto {

    @ApiModelProperty(value = "jwt 토큰", required = true, example = "ex123am45ple")
    private String jwtToken;

    @ApiModelProperty(value = "refresh 토큰", required = true, example = "ex123am45ple")
    private String refreshToken;
}