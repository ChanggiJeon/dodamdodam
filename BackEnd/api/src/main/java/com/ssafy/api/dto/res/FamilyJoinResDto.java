package com.ssafy.api.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "FamilyJoin Response")
public class FamilyJoinResDto {
    private long familyId;
}
