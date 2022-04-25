package com.ssafy.api.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "SuggestionReaction Request")
public class SuggestionReactionReqDto {

    @NotBlank
    @ApiModelProperty(value = "의견 제시 id", required = true, example = "1")
    private Long suggestionId;

    @NotBlank
    @ApiModelProperty(value = "profileId", required = true, example = "1")
    private Long profileId;

    @NotBlank
    @ApiModelProperty(value = "좋아요 여부", required = true, example = "true / false")
    private boolean isLike;

}
