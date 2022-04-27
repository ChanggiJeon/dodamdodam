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
@Schema(name = "Family In Request")
public class FamilyJoinReqDto {

    @NotBlank
    @Size(max = 10, min = 1)
    @Schema(name = "role", required = true, example = "아들")
    private String role;

    @NotBlank
    @Size(max = 10, min = 1)
    @Schema(name = "닉네임", required = true, example = "호랑이")
    private String nickname;

    @NotBlank
    @Size(max = 10, min = 1)
    @Schema(name = "생년월일", required = true, example = "1994-10-25")
    private String birthday;
}
