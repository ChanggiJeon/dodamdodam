package com.ssafy.api.dto.user;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class FindIdDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "FindId Request")
    public static class Request {
        private String name;
        private String birthday;
        private String familyCode;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "FindId Response")
    public static class Response {
        private String userId;
    }
}
