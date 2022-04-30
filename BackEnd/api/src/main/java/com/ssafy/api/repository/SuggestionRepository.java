package com.ssafy.api.repository;

import com.ssafy.api.entity.Suggestion;
import com.ssafy.api.repository.querydsl.SuggestionRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long>, SuggestionRepoCustom {

    long countSuggestionByFamily_Id(long familyId);

    Optional<Suggestion> findSuggestionById(long suggestionId);

}
