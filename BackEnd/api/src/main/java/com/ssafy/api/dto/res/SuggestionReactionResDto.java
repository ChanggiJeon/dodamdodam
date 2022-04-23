package com.ssafy.api.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Main Suggestion Response")
public class SuggestionReactionResDto {

    @ApiModelProperty(value = "좋아요 여부", example = "true/false")
    private boolean isLike;

    @ApiModelProperty(value = "profilePk", example = "1")
    private Long profilePk;
}
