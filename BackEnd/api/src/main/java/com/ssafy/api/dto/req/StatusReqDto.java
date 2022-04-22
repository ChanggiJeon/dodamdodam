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
@ApiModel(value = "Status Request")
public class StatusReqDto {

    @NotBlank
    @Size(max = 10, min = 1)
    @ApiModelProperty(value = "emotion", required = true, example = "")
    private String emotion;

    @NotBlank
    @Size(max = 10, min = 1)
    @ApiModelProperty(value = "comment", required = true, example = "")
    private String comment;
}
