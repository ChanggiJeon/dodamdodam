package com.ssafy.core.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO Model : StatusReqDto")
public class StatusReqDto {

    @NotBlank
    @Size(max = 10, min = 1)
    @Schema(description = "상태", required = true, example = "")
    private String emotion;

    @NotBlank
    @Size(max = 10, min = 1)
    @Schema(description = "오늘의 한마디", required = true, example = "")
    private String comment;
}
