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
@Schema(description = "DTO Model : NewScheduleReqDto")
public class NewScheduleReqDto {

    @NotBlank
    @Size(max = 20, min = 2)
    @Schema(description = "제목", required = true, example = "엄마 생일파티")
    private String title;

    @NotBlank
    @Size(max = 100, min = 2)
    @Schema(description = "내용", required = true, example = "샤넬백 증정식하기")
    private String content;

    @NotBlank
    @Size(max = 10, min = 10)
    @Schema(description = "시작일", required = true, example = "2022-05-23")
    private String  startDate;

    @Schema(description = "종료일", example = "2022-05-27")
    private String  endDate;
}

