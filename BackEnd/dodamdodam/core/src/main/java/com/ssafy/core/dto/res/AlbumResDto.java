package com.ssafy.core.dto.res;


import com.ssafy.core.entity.Album;
import com.ssafy.core.entity.Picture;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.lang.Nullable;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumResDto {
    @Schema(description = "앨범", example = "")
    private Album album;

    @Nullable
    @Schema(description = "메인사진", example = "")
    private Picture mainPicture;
}
