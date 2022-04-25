package com.ssafy.api.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.api.dto.res.SuggestionReactionListResDto;
import com.ssafy.api.entity.QSuggestionReaction;
import com.ssafy.api.entity.SuggestionReaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SuggestionReactionRepoCustomImpl implements SuggestionReactionRepoCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QSuggestionReaction suggestionReaction = QSuggestionReaction.suggestionReaction;

    @Override
    public SuggestionReaction findSuggestionReactionByProfileIdAndSuggestionId(Long profileId, Long suggestionId) {
        return jpaQueryFactory.select(suggestionReaction)
                .from(suggestionReaction)
                .where(suggestionReaction.profile.id.eq(profileId)
                        .and(suggestionReaction.suggestion.id.eq(suggestionId)))
                .fetchFirst();
    }

    @Override
    public List<SuggestionReactionListResDto> findSuggestionReactionBySuggestionId(Long suggestionId) {
        return jpaQueryFactory.select(Projections.fields(SuggestionReactionListResDto.class,
                        suggestionReaction.isLike,
                        suggestionReaction.profile.id.as("profileId")
                ))
                .from(suggestionReaction)
                .where(suggestionReaction.suggestion.id.eq(suggestionId))
                .fetch();
    }
}
