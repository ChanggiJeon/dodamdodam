package com.ssafy.api.dto.req;


import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumReactionReqDto {
    @ApiModelProperty(value = "emoticon", required = true, example = "아들")
    private String emoticon;
}
