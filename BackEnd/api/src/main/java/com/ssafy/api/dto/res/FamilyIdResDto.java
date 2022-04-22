package com.ssafy.api.dto.res;

import io.swagger.annotations.ApiModel;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "FamilyCodeCheck Response")
public class FamilyIdResDto {
    private long id;
}
