package com.ssafy.api.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "FamilyCode Response")
public class FamilyCodeResDto {
    private String code;
}
