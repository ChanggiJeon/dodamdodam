package com.ssafy.api.service;

import com.ssafy.core.dto.req.AlarmReqDto;
import com.ssafy.core.dto.req.SuggestionReactionReqDto;
import com.ssafy.core.dto.res.MainProfileResDto;
import com.ssafy.core.dto.res.MissionResDto;
import com.ssafy.core.dto.res.SuggestionReactionResDto;
import com.ssafy.core.dto.res.SuggestionResDto;
import com.ssafy.core.entity.Family;
import com.ssafy.core.entity.Profile;
import com.ssafy.core.entity.Suggestion;
import com.ssafy.core.exception.CustomErrorCode;
import com.ssafy.core.exception.CustomException;
import com.ssafy.core.repository.*;
import com.ssafy.core.entity.SuggestionReaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainService {

    private final ProfileRepository profileRepository;
    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;
    private final SuggestionRepository suggestionRepository;
    private final SuggestionReactionRepository suggestionReactionRepository;

    public List<MainProfileResDto> getProfileList(Long userPk) {
        long familyId = familyRepository.findFamilyIdByUserPk(userPk);

        return profileRepository.getProfileListByFamilyId(familyId).stream()
                .filter(profile -> !profile.getUserPk().equals(userPk))
                .collect(Collectors.toList());
    }

    public void createSuggestion(String text, Long userPk) {
        Family family = familyRepository.findFamilyByUserPk(userPk);

        if (family == null) {
            throw new CustomException(CustomErrorCode.INVALID_REQUEST);
        }

        if (suggestionRepository.countSuggestionByFamily_Id(family.getId()) < 3) {
            suggestionRepository.save(Suggestion.builder()
                    .family(family)
                    .text(text)
                    .build());
        } else {
            throw new CustomException(CustomErrorCode.INVALID_REQUEST, "의견제시는 가족당 최대 3개까지 입니다!");
        }
    }

    public void deleteSuggestion(Long suggestionId, Long userPk) {
        Long familyId = familyRepository.findFamilyIdByUserPk(userPk);

        Suggestion suggestion = suggestionRepository.findSuggestionById(suggestionId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.INVALID_REQUEST));

        if (suggestion.getFamily().getId() != familyId) {
            throw new CustomException(CustomErrorCode.NOT_BELONG_FAMILY);
        }

        suggestionRepository.delete(suggestion);
    }

    public List<SuggestionResDto> getSuggestionList(Long userPk) {
        long familyId = familyRepository.findFamilyIdByUserPk(userPk);

        return suggestionRepository.getSuggestionListByFamilyId(familyId)
                .stream().peek(suggestion -> {
                    if (suggestion.getSuggestionReactions().stream().allMatch(dto -> dto.getProfileId() == null)) {
                        suggestion.setSuggestionReactions(null);
                    }
                }).collect(Collectors.toList());
    }

    @Transactional
    public SuggestionReactionResDto manageSuggestionReaction(SuggestionReactionReqDto request, Long userPk) {
        //step 0. 본인의 프로필을 찾아온다.
        Profile profile = profileRepository.findProfileByUserPk(userPk);

        if (profile == null) {
            throw new CustomException(CustomErrorCode.INVALID_REQUEST);
        }

        //step 1. 주어진 pk로 의견을 찾아온다.
        Suggestion suggestion = suggestionRepository.findSuggestionById(request.getSuggestionId())
                .orElseThrow(() -> new CustomException(CustomErrorCode.INVALID_REQUEST));

        //step 2. 본인의 가족번호를 찾아온다.
        long familyId = familyRepository.findFamilyIdByUserPk(userPk);

        //step 3. 본인 가족 의견이 아니면 Exception 발생
        if (familyId != suggestion.getFamily().getId()) {
            throw new CustomException(CustomErrorCode.NOT_BELONG_FAMILY);
        }

        //step 4. 본인 리엑션을 찾아본다.
        SuggestionReaction suggestionReaction =
                suggestionReactionRepository.findSuggestionReactionByProfileIdAndSuggestionId(
                        profile.getId(), request.getSuggestionId());


        //step 4. 본인 리엑션이 없다면 새롭게 등록하고(like, dislike count 갱신), 기존 반응이 있으면 변경됐을 때만 Update
        if (suggestionReaction == null) {
            suggestionReactionRepository.save(SuggestionReaction.builder()
                    .profile(profile)
                    .suggestion(suggestion)
                    .isLike(request.isLike())
                    .build());

            if (request.isLike()) {
                suggestion.updateLikeCount(1);
            } else {
                suggestion.updateDislikeCount(1);
            }

            suggestion = suggestionRepository.save(suggestion);

        } else if (suggestionReaction.getIsLike() != request.isLike()) {
            suggestionReaction.setIsLike(request.isLike());
            suggestionReactionRepository.save(suggestionReaction);

            int updateCount = request.isLike() ? +1 : -1;
            suggestion.updateLikeCount(updateCount);
            suggestion.updateDislikeCount(updateCount * -1);

            suggestion = suggestionRepository.save(suggestion);
        } else {
            suggestionReactionRepository.delete(suggestionReaction);
            if (request.isLike()) {
                suggestion.updateLikeCount(-1);
            } else {
                suggestion.updateDislikeCount(-1);
            }

            suggestion = suggestionRepository.save(suggestion);
        }

        return SuggestionReactionResDto.builder()
                .suggestionId(request.getSuggestionId())
                .like(suggestion.getLikeCount())
                .dislike(suggestion.getDislikeCount())
                .build();
    }


    public MissionResDto getTodayMission(long userPk) {
        Profile profile = profileRepository.findProfileByUserPk(userPk);
        return MissionResDto.builder()
                .missionContent(profile.getMission_content())
                .build();
    }

    public String getTargetFcmToken(Long targetId) {
        return userRepository.findUserFcmTokenByProfileId(targetId);

    }
    public String getOneProfileNickname(Long userPk) {
        return profileRepository.findProfileByUserPk(userPk).getNickname();
    }
}
