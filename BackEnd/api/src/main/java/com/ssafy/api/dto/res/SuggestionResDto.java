package com.ssafy.api.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Main Suggestion Response")
public class SuggestionResDto {

    @Schema(description = "의견 제시 Id", example = "1")
    private Long suggestionId;

    @Schema(description = "의견 내용", example = "예시입니다.")
    private String text;

    private List<SuggestionReactionListResDto> suggestionReactions;

}
