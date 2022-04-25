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
@ApiModel(value = "Family In Request")
public class FamilyJoinReqDto {

    @NotBlank
    @Size(max = 10, min = 1)
    @ApiModelProperty(value = "role", required = true, example = "아들")
    private String role;

    @NotBlank
    @Size(max = 10, min = 1)
    @ApiModelProperty(value = "닉네임", required = true, example = "호랑이")
    private String nickname;

    @NotBlank
    @Size(max = 10, min = 1)
    @ApiModelProperty(value = "생년월일", required = true, example = "1994-10-25")
    private String birthday;
}
