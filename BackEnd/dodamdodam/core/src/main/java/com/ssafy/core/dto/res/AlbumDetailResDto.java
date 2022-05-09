package com.ssafy.core.dto.res;


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

    @Schema(description = "날짜", example = "")
    private String date;

    @Schema(description = "사진", example = "")
    private List<AlbumPictureListResDto> pictures;

    @Schema(description = "해시태그", example = "")
    private List<AlbumHashTagListResDto> hashTags;

    @Schema(description = "앨범 리액션", example = "")
    private List<AlbumReactionListResDto> albumReactions;



}
