package com.ssafy.core.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO Model : SuggestionReactionReqDto")
public class SuggestionReactionReqDto {

    @NotBlank
    @Schema(description = "의견 제시 id", required = true, example = "1")
    private Long suggestionId;

    @NotBlank
    @Schema(description = "좋아요 여부", required = true, example = "true / false")
    private boolean isLike;

}
