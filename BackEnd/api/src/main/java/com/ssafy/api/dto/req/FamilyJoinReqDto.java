package com.ssafy.api.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO Model : FamilyJoinReqDto")
public class FamilyJoinReqDto {

    @NotBlank
    @Size(max = 10, min = 1)
    @Schema(name = "role", description = "역할", required = true, example = "아들")
    private String role;

    @NotBlank
    @Size(max = 10, min = 1)
    @Schema(name = "nickname", description = "닉네임", required = true, example = "호랑이")
    private String nickname;

    @NotBlank
    @Size(max = 10, min = 1)
    @Schema(name = "birthday", description = "생년월일", required = true, example = "1994-10-25")
    private String birthday;

    private MultipartFile image;
}
