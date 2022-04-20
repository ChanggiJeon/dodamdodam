package com.ssafy.api.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "SingIn Response")
public class SignInResDto {
    @ApiModelProperty(value = "아이디", required = true, example = "ssafy")
    private String userId;

    @ApiModelProperty(value = "이름", required = true, example = "싸피")
    private String name;

    @Nullable
    @ApiModelProperty(value = "생일", required = true, example = "ssafy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate birthday;

    @ApiModelProperty(value = "jwt 토큰", required = true, example = "ex123am45ple")
    private String jwtToken;

    @ApiModelProperty(value = "refresh 토큰", required = true, example = "ex123am45ple")
    private String refreshToken;

    @Nullable
    @ApiModelProperty(value = "권한", required = true, example = "ROLE_USER")
    private String authority;
}