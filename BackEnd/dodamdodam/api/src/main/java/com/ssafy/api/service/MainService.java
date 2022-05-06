package com.ssafy.api.service;

import com.ssafy.core.dto.req.AlarmReqDto;
import com.ssafy.core.dto.req.SuggestionReactionReqDto;
import com.ssafy.core.dto.res.*;
import com.ssafy.core.entity.*;
import com.ssafy.core.exception.CustomErrorCode;
import com.ssafy.core.exception.CustomException;
import com.ssafy.core.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    private final AlarmRepository alarmRepository;

    public List<MainProfileResDto> getProfileList(Long userPk) {
        long familyId = familyRepository.findFamilyIdByUserPk(userPk);

        return profileRepository.findProfileListByFamilyId(familyId).stream()
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


    public MissionResDto findTodayMission(long userPk) {
        return profileRepository.findTodayMissionByUserPk(userPk);
    }

    public String getTargetFcmToken(Profile target) {
        String fcmToken = userRepository.findUserFcmTokenByProfile(target);
        if (fcmToken == null) {
            throw new CustomException(CustomErrorCode.INVALID_REQUEST, "fcm 토큰이 없습니다.");
        }
        return fcmToken;

    }
    public Profile getProfileByUserPk(Long userPk) {
        Profile profile = profileRepository.findProfileByUserPk(userPk);
        if (profile == null) {
            throw new CustomException(CustomErrorCode.INVALID_REQUEST, "소속된 그룹이 없습니다.");
        }
        return profile;
    }

    public Profile getProfileByProfilePk(long targetProfileId) {
        Profile profile = profileRepository.findProfileById(targetProfileId);
        if (profile == null) {
            throw new CustomException(CustomErrorCode.INVALID_REQUEST, "소속된 그룹이 없습니다.");
        }
        return profile;
    }

    public void recordAlarmCount(Profile me, AlarmReqDto alarmReq) {
        Profile target = profileRepository.findProfileById(alarmReq.getTargetProfileId());
        Alarm alarm = alarmRepository.findAlarmByProfileAndTarget(me, target, alarmReq.getContent());
        if (alarm == null) {
            alarmRepository.save(Alarm.builder()
                    .content(alarmReq.getContent())
                    .me(me)
                    .target(target)
                    .count(1)
                    .build()
            );
        } else {
            alarm.setCount(alarm.getCount()+1);
            alarmRepository.save(alarm);
        }
    }

    public List<AlarmResDto> getAlarmList(Profile me, Profile target) {
        ArrayList<String> contentList = new ArrayList<>(Arrays.asList("사랑해", "보고싶어", "감사해요!", "이따 봐용~", "오늘도 화이팅", "밥 먹자~"));
        List<AlarmResDto> dtoList = alarmRepository.findAlarmByProfileAndTargetOrderByCount(me, target);
        for (AlarmResDto alarmResDto : dtoList) {
            contentList.remove(alarmResDto.getContent());
        }
        Collections.shuffle(contentList);
        for (String content : contentList) {
            AlarmResDto dto = new AlarmResDto();
            dto.setContent(content);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public void meAndTargetFamilyCheck(Profile me, Profile target) {
        if (me.getFamily().getId() != target.getFamily().getId()) {
            throw new CustomException(CustomErrorCode.INVALID_REQUEST, "다른 가족 그룹 인원에게 알람 전송 불가능");
        }
    }
}
