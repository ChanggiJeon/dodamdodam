package com.ssafy.api.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO Model : MissionReqDto")
public class MissionReqDto {

    @NotBlank
    @Size(max = 10, min = 1)
    @Schema(description = "미션내용", required = true, example = "")
    private String missionContent;
}
