package com.ssafy.api.dto.req;


import com.ssafy.api.entity.HashTag;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumReqDto {

    @NotBlank
    @ApiModelProperty(value = "hashtag", required = true, example = "아들")
    private List<HashTag> hashTags;

    @NotBlank
    @Size(max = 10, min = 1)
    @ApiModelProperty(value = "앨범날짜", required = true, example = "2022-04-20")
    private String date;


}
