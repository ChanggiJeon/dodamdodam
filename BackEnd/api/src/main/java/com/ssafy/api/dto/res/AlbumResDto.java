package com.ssafy.api.dto.res;


import com.ssafy.api.entity.Album;
import com.ssafy.api.entity.Picture;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.lang.Nullable;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumResDto {
    @Schema(name = "앨범", example = "")
    private Album album;

    @Nullable
    @Schema(name = "메인사진", example = "")
    private Picture mainPicture;
}
