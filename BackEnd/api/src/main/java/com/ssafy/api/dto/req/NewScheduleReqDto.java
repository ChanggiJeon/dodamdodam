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
@Schema(name = "Schedule Request")
public class NewScheduleReqDto {

    @NotBlank
    @Size(max = 20, min = 2)
    @Schema(name = "title", required = true, example = "엄마 생일파티")
    private String title;

    @NotBlank
    @Size(max = 100, min = 2)
    @Schema(name = "content", required = true, example = "샤넬백 증정식하기")
    private String content;

    @NotBlank
    @Size(max = 10, min = 10)
    @Schema(name = "content", required = true, example = "2022-05-23")
    private String  startDate;

    @Schema(name = "content", example = "2022-05-27")
    private String  endDate;
}

