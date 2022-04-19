package com.ssafy.api.dto;

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
    @AllArgsConstructor
    public static class Request {

        @NotBlank
        @Size(max = 20, min = 5)
        @ApiModelProperty(value = "아이디", required = true, example = "ssafy")
        private String userId;

        @NotBlank
        @Size(max = 20, min = 8)
        @ApiModelProperty(value = "비밀번호", required = true, example = "ssafy61!")
        private String password;

    }

    @Builder
    @AllArgsConstructor
    public static class Response {
        private String userId;
        private String name;
        @Nullable
        private LocalDateTime birthday;
        private String jwtToken;
        private String refreshToken;
        @Nullable
        private String authority;
    }
}
