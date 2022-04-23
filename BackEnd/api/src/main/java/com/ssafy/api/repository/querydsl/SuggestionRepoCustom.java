package com.ssafy.api.repository.querydsl;

import com.ssafy.api.dto.res.SuggestionResDto;

import java.util.List;

public interface SuggestionRepoCustom {

    List<SuggestionResDto> getSuggestionListByFamilyId(Long familyId);
}
