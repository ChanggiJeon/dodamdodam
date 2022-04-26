package com.ssafy.api.dto.res;


import com.ssafy.api.entity.HashTag;
import com.ssafy.api.entity.Picture;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumHashTagListResDto {
    @ApiModelProperty(value = "해시태그", example = "#여름")
    private String text;

    public List<AlbumHashTagListResDto> fromEntity(List<HashTag> hashTags){
        List<AlbumHashTagListResDto> result = new ArrayList<>();
        for(int i = 0 ; i<hashTags.size() ; i++) {
            AlbumHashTagListResDto albumHashTagListResDto = AlbumHashTagListResDto.builder()
                    .text(hashTags.get(i).getText())
                    .build();
            result.add(albumHashTagListResDto);
        }
        return result;
    }
}
