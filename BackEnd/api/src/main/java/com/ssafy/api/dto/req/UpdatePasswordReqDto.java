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
@Schema(name = "UpdatePassword Request")
public class UpdatePasswordReqDto {

    @NotBlank
    @Size(max = 20, min = 5)
    @Schema(description = "아이디", required = true, example = "ssafy")
    private String userId;

    @NotBlank
    @Size(max = 20, min = 8)
    @Schema(description = "비밀번호", required = true, example = "ssafy61!")
    private String password;
}