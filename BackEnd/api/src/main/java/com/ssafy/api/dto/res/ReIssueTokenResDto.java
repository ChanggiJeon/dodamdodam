package com.ssafy.api.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ReIssueToken Response")
public class ReIssueTokenResDto {

    @Schema(name = "jwt 토큰", example = "ex123am45ple")
    private String jwtToken;

    @Schema(name = "refresh 토큰", example = "ex123am45ple")
    private String refreshToken;
}