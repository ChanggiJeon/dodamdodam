package com.ssafy.core.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "DTO Model : FindIdReqDto")
public class FindIdReqDto {

    @NotBlank
    @Size(max = 10, min = 1)
    @Schema(description = "이름", required = true, example = "싸피")
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @NotBlank
    @Size(max = 15, min = 15)
    @Schema(description = "가족 코드", required = true, example = "SSAFYFINALPROJE")
    private String familyCode;
}