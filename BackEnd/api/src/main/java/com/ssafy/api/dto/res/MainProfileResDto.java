package com.ssafy.api.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Main Profile Response")
public class MainProfileResDto {

    @ApiModelProperty(value = "유저Pk", example = "1")
    private Long userPk;

    @ApiModelProperty(value = "프로필 이미지 경로", example = "example/example1.png")
    private String imagePath;

    @ApiModelProperty(value = "프로필 역할", example = "아빠")
    private String role;

    @ApiModelProperty(value = "상태 이모티콘 String", example = ":smile")
    private String emotion;

    @ApiModelProperty(value = "오늘의 한마디", example = "예시입니다!")
    private String comment;

}
