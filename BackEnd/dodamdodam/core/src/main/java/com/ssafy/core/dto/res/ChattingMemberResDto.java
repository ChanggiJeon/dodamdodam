package com.ssafy.core.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ChattingMember Response")
public class ChattingMemberResDto {

    @Schema(description = "프로필 아이디", example = "")
    private Long profileId;

    @Schema(description = "프로필 사진 경로", example = "")
    private String profileImage;

    @Schema(description = "프로필 닉네임", example = "")
    private String nickname;
}
