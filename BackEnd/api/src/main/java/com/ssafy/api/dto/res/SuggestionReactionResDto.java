package com.ssafy.api.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Suggestion Reaction Response")
public class SuggestionReactionResDto {

    @ApiModelProperty(value = "의견 제시 id", example = "1")
    private Long suggestionId;

    @ApiModelProperty(value = "좋아요 갯수", example = "2")
    private Long like;

    @ApiModelProperty(value = "싫어요 갯수", example = "3")
    private Long dislike;
}
