package com.ssafy.api.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Suggestion Reaction List Response")
public class SuggestionReactionListResDto {

    @Schema(name = "좋아요 여부", example = "true/false")
    private boolean isLike;

    @Schema(name = "profileId", example = "1")
    private Long profileId;
}
