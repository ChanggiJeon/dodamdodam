package com.ssafy.api.dto.user;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@ApiModel("updatePassword")
public class updatePasswordDto {

    @Getter
    @AllArgsConstructor
    public static class Request {
        private String userId;
        private String password;
    }
}
