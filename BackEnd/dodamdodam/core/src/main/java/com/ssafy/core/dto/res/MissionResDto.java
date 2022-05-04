package com.ssafy.core.dto.res;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO Model : MissionResDto")
public class MissionResDto {

    @Schema(description = "미션내용", example = "엄마를 안아주세요!")
    private String missionContent;
}
