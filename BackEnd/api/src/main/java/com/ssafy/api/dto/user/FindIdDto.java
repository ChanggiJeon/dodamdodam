package com.ssafy.api.dto.user;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;

@ApiModel("ReIssueToken")
public class FindIdDto {

    @Builder
    @AllArgsConstructor
    public static class Request{
        private String name;
        private String birthday;
        private String familyCode;
    }

    @AllArgsConstructor
    public static class Response{
        private String userId;
    }
}
