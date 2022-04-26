package com.ssafy.api.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Main Profile Response")
public class MainProfileResDto {

    @Schema(name = "유저Pk", example = "1")
    private Long userPk;

    @Schema(name = "프로필 이미지 경로", example = "example/example1.png")
    private String imagePath;

    @Schema(name = "프로필 역할", example = "아빠")
    private String role;

    @Schema(name = "상태 이모티콘 String", example = ":smile")
    private String emotion;

    @Schema(name = "오늘의 한마디", example = "예시입니다!")
    private String comment;

}
