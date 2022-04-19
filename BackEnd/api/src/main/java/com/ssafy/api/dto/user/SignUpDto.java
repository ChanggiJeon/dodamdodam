package com.ssafy.api.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ApiModel("SingUp")
public class SignUpDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {

        @NotBlank
        @Size(max = 20, min = 5)
        @ApiModelProperty(value = "아이디", required = true, example = "ssafy")
        private String userId;

        @NotBlank
        @Size(max = 20, min = 8)
        @ApiModelProperty(value = "비밀번호", required = true, example = "ssafy61!")
        private String password;

        @NotBlank
        @Size(max = 10, min = 1)
        @ApiModelProperty(value = "이름", required = true, example = "싸피")
        private String name;

    }
}
