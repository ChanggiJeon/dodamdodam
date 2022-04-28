package com.ssafy.api.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumReactionReqDto {
    @Schema(description = "이모티콘", required = true, example = "아들")
    private String emoticon;
}