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
@Schema(description = "DTO Model : SignUpReqDto")
public class SignUpReqDto {

    @NotBlank
    @Size(max = 20, min = 4)
    @Schema(description = "아이디", required = true, example = "ssafy")
    private String userId;

    @NotBlank
    @Size(max = 20, min = 8)
    @Schema(description = "비밀번호", required = true, example = "ssafy61!")
    private String password;

    @NotBlank
    @Size(max = 10, min = 1)
    @Schema(description = "이름", required = true, example = "싸피")
    private String name;

}