package com.ssafy.core.repository.querydsl;

import com.ssafy.core.dto.res.SuggestionReactionListResDto;
import com.ssafy.core.entity.SuggestionReaction;

import java.util.List;

public interface SuggestionReactionRepoCustom {

    List<SuggestionReactionListResDto> findSuggestionReactionBySuggestionId(Long suggestionId);

    SuggestionReaction findSuggestionReactionByProfileIdAndSuggestionId(Long profileId, Long suggestionId);
}
