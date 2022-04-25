package com.ssafy.api.dto.res;

import io.swagger.annotations.ApiModel;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "FamilyJoin Response")
public class FamilyJoinResDto {
    private long familyId;
}
