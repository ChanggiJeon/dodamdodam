package com.ssafy.api.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Schedule Request")
public class NewScheduleReqDto {

    @NotBlank
    @Size(max = 20, min = 2)
    @ApiModelProperty(value = "title", required = true, example = "엄마 생일파티")
    private String title;

    @NotBlank
    @Size(max = 100, min = 2)
    @ApiModelProperty(value = "content", required = true, example = "샤넬백 증정식하기")
    private String content;

    @NotBlank
    @Size(max = 10, min = 10)
    @ApiModelProperty(value = "content", required = true, example = "2022-05-23")
    private String  startDate;

    @ApiModelProperty(value = "content", example = "2022-05-27")
    private String  endDate;
}

