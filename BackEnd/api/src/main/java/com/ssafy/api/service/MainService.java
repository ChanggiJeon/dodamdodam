package com.ssafy.api.service;

import com.ssafy.api.dto.res.MainProfileResDto;
import com.ssafy.api.dto.res.SuggestionResDto;
import com.ssafy.api.entity.Family;
import com.ssafy.api.entity.Suggestion;
import com.ssafy.api.exception.CustomErrorCode;
import com.ssafy.api.exception.CustomException;
import com.ssafy.api.repository.FamilyRepository;
import com.ssafy.api.repository.ProfileRepository;
import com.ssafy.api.repository.SuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainService {

    private final ProfileRepository profileRepository;
    private final FamilyRepository familyRepository;
    private final SuggestionRepository suggestionRepository;

    public List<MainProfileResDto> getProfileList(Long familyId, Long userPK) {
        return profileRepository.getProfileListByFamilyId(familyId).stream()
                .filter(profile -> !profile.getUserPk().equals(userPK))
                .collect(Collectors.toList());
    }

    public void createSuggestion(String text, Long userPk) {
        Family family = familyRepository.findFamilyByUserPk(userPk);

        if(family == null){
            throw new CustomException(CustomErrorCode.INVALID_REQUEST);
        }

        suggestionRepository.save(Suggestion.builder()
                .family(family)
                .text(text)
                .build());
    }

    public List<SuggestionResDto> getSuggestionList(Long familyId) {
        return suggestionRepository.getSuggestionListByFamilyId(familyId);
    }
}
