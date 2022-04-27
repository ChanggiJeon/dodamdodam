package com.ssafy.api.dto.res;


import com.ssafy.api.entity.AlbumReaction;
import com.ssafy.api.entity.HashTag;
import com.ssafy.api.entity.Picture;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDetailResDto {
//date 해시태그 사진 가족별 리액션

    @ApiModelProperty(value = "date", example = "")
    private String date;

    @ApiModelProperty(value = "사진", example = "")
    private List<AlbumPictureListResDto> pictures;

    @ApiModelProperty(value = "해시태그", example = "")
    private List<AlbumHashTagListResDto> hashTags;

    @ApiModelProperty(value = "앨범 리액션", example = "")
    private List<AlbumReactionListResDto> albumReactions;



}
