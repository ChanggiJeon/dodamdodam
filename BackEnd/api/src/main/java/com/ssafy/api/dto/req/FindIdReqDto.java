package com.ssafy.api.dto.req;

import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "FindId Request")
public class FindIdReqDto {

    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String birthday;
    private String familyCode;
}