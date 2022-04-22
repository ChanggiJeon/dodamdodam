package com.ssafy.api.dto.req;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ProfileReqDto {

//    @ApiModelProperty(value = "프로필이미지", required = false, example = "")
//    private MultipartFile profileImage;

    @NotBlank
    @Size(max = 10, min = 1)
    @ApiModelProperty(value = "role", required = true, example = "아들")
    private String role;

    @NotBlank
    @Size(max = 10, min = 1)
    @ApiModelProperty(value = "닉네임", required = true, example = "싸피")
    private String nickname;

    @NotBlank
    @Size(max = 10, min = 1)
    @ApiModelProperty(value = "생년월일", required = true, example = "1995-08-20")
    private String birthday;
}
