package com.ssafy.api.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.lang.Nullable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SingIn Response")
public class SignInResDto {

    @Schema(name = "이름", example = "싸피")
    private String name;

    @Schema(name = "accessToken", example = "SSAFYSSAFY12345")
    private String jwtToken;

    @Schema(name = "refreshToken", example = "1234512345SSAFY")
    private String refreshToken;

    @Nullable
    @Schema(name = "profileId", example = "1 / null")
    private Long profileId;

    @Nullable
    @Schema(name = "familyId", example = "1 /  null")
    private Long familyId;
}