package com.ssafy.dodamdodam.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@ApiModel("User")
public class UserRequest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class signUp {

        @NotBlank
        @Max(20)
        @ApiModelProperty(value = "아이디", required = true, example = "ssafy")
        private String userId;

        @NotBlank
        @Max(20)
        @ApiModelProperty(value = "비밀번호", required = true, example = "ssafy1!")
        private String password;

        @NotBlank
        @Max(10)
        @ApiModelProperty(value = "이름", required = true, example = "싸피")
        private String name;

    }

}
