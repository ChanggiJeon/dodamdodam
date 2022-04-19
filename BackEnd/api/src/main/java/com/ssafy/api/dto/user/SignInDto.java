package com.ssafy.api.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@ApiModel("SingIn")
public class SignInDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "SingIn Request")
    public static class Request {

        @NotBlank
        @Size(max = 20, min = 4)
        @ApiModelProperty(value = "아이디", required = true, example = "ssafy")
        private String userId;

        @NotBlank
        @Size(max = 20, min = 8)
        @ApiModelProperty(value = "비밀번호", required = true, example = "ssafy61!")
        private String password;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "SingIn Response")
    public static class Response {
        @ApiModelProperty(value = "아이디", required = true, example = "ssafy")
        private String userId;

        @ApiModelProperty(value = "이름", required = true, example = "싸피")
        private String name;

        @Nullable
        @ApiModelProperty(value = "생일", required = true, example = "ssafy")
        private LocalDateTime birthday;

        @ApiModelProperty(value = "jwt 토큰", required = true, example = "ex123am45ple")
        private String jwtToken;

        @ApiModelProperty(value = "refresh 토큰", required = true, example = "ex123am45ple")
        private String refreshToken;

        @Nullable
        @ApiModelProperty(value = "권한", required = true, example = "ROLE_USER")
        private String authority;
    }
}
