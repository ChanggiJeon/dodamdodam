package com.ssafy.api.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UpdatePasswordDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "UpdatePassword Request")
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
}
