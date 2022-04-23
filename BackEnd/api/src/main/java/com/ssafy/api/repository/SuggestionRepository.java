package com.ssafy.api.repository;

import com.ssafy.api.entity.Suggestion;
import com.ssafy.api.repository.querydsl.SuggestionRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long>, SuggestionRepoCustom {

}
