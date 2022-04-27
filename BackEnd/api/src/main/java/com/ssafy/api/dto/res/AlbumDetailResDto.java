package com.ssafy.api.dto.res;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDetailResDto {
//date 해시태그 사진 가족별 리액션

    @Schema(name = "date", example = "")
    private String date;

    @Schema(name = "사진", example = "")
    private List<AlbumPictureListResDto> pictures;

    @Schema(name = "해시태그", example = "")
    private List<AlbumHashTagListResDto> hashTags;

    @Schema(name = "앨범 리액션", example = "")
    private List<AlbumReactionListResDto> albumReactions;



}
