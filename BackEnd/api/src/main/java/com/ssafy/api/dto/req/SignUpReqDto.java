package com.ssafy.api.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "SignUp Request")
public class SignUpReqDto {

    @NotBlank
    @Size(max = 20, min = 4)
    @ApiModelProperty(value = "아이디", required = true, example = "ssafy")
    private String userId;

    @NotBlank
    @Size(max = 20, min = 8)
    @ApiModelProperty(value = "비밀번호", required = true, example = "ssafy61!")
    private String password;

    @NotBlank
    @Size(max = 10, min = 1)
    @ApiModelProperty(value = "이름", required = true, example = "싸피")
    private String name;

}