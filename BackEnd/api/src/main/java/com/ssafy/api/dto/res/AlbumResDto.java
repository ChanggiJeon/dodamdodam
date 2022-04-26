package com.ssafy.api.dto.res;


import com.ssafy.api.entity.Album;
import com.ssafy.api.entity.Picture;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumResDto {
    @ApiModelProperty(value = "앨범", example = "")
    private Album album;

    @Nullable
    @ApiModelProperty(value = "메인사진", example = "")
    private Picture mainPicture;
}
