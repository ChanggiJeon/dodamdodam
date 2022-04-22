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
@ApiModel(value = "FindId Request")
public class FindIdReqDto {

    @NotBlank
    @Size(max = 10, min = 1)
    @ApiModelProperty(value = "이름", required = true, example = "싸피")
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @NotBlank
    @Size(max = 15, min = 15)
    @ApiModelProperty(value = "가족 코드", required = true, example = "SSAFYFINALPROJE")
    private String familyCode;
}