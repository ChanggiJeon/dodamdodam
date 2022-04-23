package com.ssafy.api.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Main Suggestion Response")
public class SuggestionResDto {

    @ApiModelProperty(value = "의견 제시 Id", example = "1")
    private Long suggestionId;

    @ApiModelProperty(value = "의견 내용", example = "예시입니다.")
    private String text;

    @ApiModelProperty(value = "의견 반응 List", example = "{profilePK :1, isLike: false")
    private List<SuggestionReactionResDto> suggestionReactions;

}
