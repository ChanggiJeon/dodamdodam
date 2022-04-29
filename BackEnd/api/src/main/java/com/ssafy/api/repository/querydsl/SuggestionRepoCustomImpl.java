package com.ssafy.api.repository.querydsl;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.api.dto.res.SuggestionReactionListResDto;
import com.ssafy.api.dto.res.SuggestionResDto;
import com.ssafy.api.entity.QSuggestion;
import com.ssafy.api.entity.QSuggestionReaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SuggestionRepoCustomImpl implements SuggestionRepoCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QSuggestion suggestion = QSuggestion.suggestion;
    QSuggestionReaction suggestionReaction = QSuggestionReaction.suggestionReaction;

    @Override
    public List<SuggestionResDto> getSuggestionListByFamilyId(Long familyId) {
        return jpaQueryFactory.selectFrom(suggestion)
                .leftJoin(suggestionReaction)
                .on(suggestion.id.eq(suggestionReaction.suggestion.id))
                .where(suggestion.family.id.eq(familyId))
                .transform(
                        GroupBy.groupBy(suggestion)
                                .list(Projections.fields(SuggestionResDto.class,
                                        suggestion.id.as("suggestionId"),
                                        suggestion.text,
                                        suggestion.likeCount,
                                        suggestion.dislikeCount,
                                        GroupBy.list(
                                                Projections.fields(
                                                        SuggestionReactionListResDto.class,
                                                        suggestionReaction.profile.id.as("profileId"),
                                                        suggestionReaction.isLike
                                                )
                                        ).as("suggestionReactions")
                                )));
    }
}