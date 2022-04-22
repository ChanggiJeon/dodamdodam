package com.ssafy.api.dto.req;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Mission Request")
public class MissionReqDto {

    @NotBlank
    @Size(max = 10, min = 1)
    @ApiModelProperty(value = "missionContent", required = true, example = "")
    private String missionContent;
}
