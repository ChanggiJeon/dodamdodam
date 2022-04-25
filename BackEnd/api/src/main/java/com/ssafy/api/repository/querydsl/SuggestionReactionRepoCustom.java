package com.ssafy.api.repository.querydsl;

import com.ssafy.api.dto.req.SuggestionReactionReqDto;
import com.ssafy.api.dto.res.SuggestionReactionListResDto;
import com.ssafy.api.entity.SuggestionReaction;

import java.util.List;

public interface SuggestionReactionRepoCustom {

    List<SuggestionReactionListResDto> findSuggestionReactionBySuggestionId(Long suggestionId);

    SuggestionReaction findSuggestionReactionByProfileIdAndSuggestionId(Long profileId, Long suggestionId);
}
