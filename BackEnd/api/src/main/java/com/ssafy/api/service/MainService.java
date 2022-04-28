package com.ssafy.api.service;

import com.ssafy.api.dto.req.SuggestionReactionReqDto;
import com.ssafy.api.dto.res.MainProfileResDto;
import com.ssafy.api.dto.res.SuggestionReactionListResDto;
import com.ssafy.api.dto.res.SuggestionReactionResDto;
import com.ssafy.api.dto.res.SuggestionResDto;
import com.ssafy.api.entity.Family;
import com.ssafy.api.entity.Profile;
import com.ssafy.api.entity.Suggestion;
import com.ssafy.api.entity.SuggestionReaction;
import com.ssafy.api.exception.CustomException;
import com.ssafy.api.repository.FamilyRepository;
import com.ssafy.api.repository.ProfileRepository;
import com.ssafy.api.repository.SuggestionReactionRepository;
import com.ssafy.api.repository.SuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.ssafy.api.exception.CustomErrorCode.INVALID_REQUEST;

@Service
@RequiredArgsConstructor
public class MainService {

    private final ProfileRepository profileRepository;
    private final FamilyRepository familyRepository;
    private final SuggestionRepository suggestionRepository;
    private final SuggestionReactionRepository suggestionReactionRepository;

    public List<MainProfileResDto> getProfileList(Long familyId, Long userPK) {
        return profileRepository.getProfileListByFamilyId(familyId).stream()
                .filter(profile -> !profile.getUserPk().equals(userPK))
                .collect(Collectors.toList());
    }

    public void createSuggestion(String text, Long userPk) {
        Family family = familyRepository.findFamilyByUserPk(userPk);

        if (family == null) {
            throw new CustomException(INVALID_REQUEST);
        }

        if (suggestionRepository.countSuggestionByFamily_Id(family.getId()) < 3) {
            suggestionRepository.save(Suggestion.builder()
                    .family(family)
                    .text(text)
                    .build());
        }else{
            throw new CustomException(INVALID_REQUEST, "의견제시는 가족당 최대 3개까지 입니다!");
        }

    }

    public List<SuggestionResDto> getSuggestionList(Long familyId) {
        return suggestionRepository.getSuggestionListByFamilyId(familyId)
                .stream().peek(suggestion -> {
                    if (suggestion.getSuggestionReactions().stream().allMatch(dto -> dto.getProfileId() == null)) {
                        suggestion.setSuggestionReactions(null);
                    }
                }).collect(Collectors.toList());
    }

    public SuggestionReactionResDto manageSuggestionReaction(SuggestionReactionReqDto request) {

        //step 1. 본인 리엑션을 찾아본다.
        SuggestionReaction suggestionReaction =
                suggestionReactionRepository.findSuggestionReactionByProfileIdAndSuggestionId(
                        request.getProfileId(), request.getSuggestionId());

        //step 2. 없다면 새롭게 등록하고, 기존 반응이 있으면 변경됐을 때만 Update
        if (suggestionReaction == null) {
            Suggestion suggestion = suggestionRepository.findById(request.getSuggestionId())
                    .orElseThrow(() -> new CustomException(INVALID_REQUEST));

            Profile profile = profileRepository.findById(request.getProfileId())
                    .orElseThrow(() -> new CustomException(INVALID_REQUEST));

            suggestionReactionRepository.save(SuggestionReaction.builder()
                    .profile(profile)
                    .suggestion(suggestion)
                    .isLike(request.isLike())
                    .build());

        } else if (suggestionReaction.isLike() != request.isLike()) {
            suggestionReaction.setLike(request.isLike());
            suggestionReactionRepository.save(suggestionReaction);
        }

        //step 2. 리엑션을 전부 찾아서 like, dislike를 count한다.
        List<SuggestionReactionListResDto> reactions =
                suggestionReactionRepository.findSuggestionReactionBySuggestionId(request.getSuggestionId());
        long like = 0L;
        long disLike = 0L;

        for (SuggestionReactionListResDto reaction : reactions) {
            System.out.println(reaction.isLike());
            if (reaction.isLike()) {
                like += 1;
            } else {
                disLike += 1;
            }
        }

        return SuggestionReactionResDto.builder()
                .suggestionId(request.getSuggestionId())
                .like(like)
                .dislike(disLike)
                .build();
    }
}
