package com.ssafy.api.dto.res;

import com.ssafy.api.entity.AlbumReaction;
import com.ssafy.api.entity.Picture;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumReactionListResDto {
    @ApiModelProperty(value = "이모티콘", example = "예시입니다.")
    private String emoticon;

    @ApiModelProperty(value = "프로필 사진", example = "예시입니다.")
    private String imagePath;

    @ApiModelProperty(value = "역할", example = "예시입니다.")
    private String role;



}
