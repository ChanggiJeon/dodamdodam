package com.ssafy.api.repository;

import com.ssafy.api.entity.SuggestionReaction;
import com.ssafy.api.repository.querydsl.SuggestionReactionRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuggestionReactionRepository extends JpaRepository<SuggestionReaction, Long>, SuggestionReactionRepoCustom {

}
