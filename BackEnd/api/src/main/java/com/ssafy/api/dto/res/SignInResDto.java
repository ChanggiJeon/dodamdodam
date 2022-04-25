package com.ssafy.api.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.lang.Nullable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "SingIn Response")
public class SignInResDto {

    @ApiModelProperty(value = "이름", example = "싸피")
    private String name;

    @ApiModelProperty(value = "accessToken", example = "SSAFYSSAFY12345")
    private String jwtToken;

    @ApiModelProperty(value = "refreshToken", example = "1234512345SSAFY")
    private String refreshToken;

    @Nullable
    @ApiModelProperty(value = "profileId", example = "1 / null")
    private Long profileId;

    @Nullable
    @ApiModelProperty(value = "familyId", example = "1 /  null")
    private Long familyId;
}