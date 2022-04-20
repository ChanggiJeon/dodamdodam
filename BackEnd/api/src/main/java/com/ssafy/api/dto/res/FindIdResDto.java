package com.ssafy.api.dto.res;

import io.swagger.annotations.ApiModel;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "FindId Response")
public class FindIdResDto {
    private String userId;
}