package com.ssafy.api.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Suggestion Reaction List Response")
public class SuggestionReactionListResDto {

    @ApiModelProperty(value = "좋아요 여부", example = "true/false")
    private boolean isLike;

    @ApiModelProperty(value = "profileId", example = "1")
    private Long profileId;
}
