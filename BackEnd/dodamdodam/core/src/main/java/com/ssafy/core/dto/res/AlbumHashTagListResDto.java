package com.ssafy.core.dto.res;


import com.ssafy.core.entity.HashTag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumHashTagListResDto {
    @Schema(description = "해시태그", example = "#여름")
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
