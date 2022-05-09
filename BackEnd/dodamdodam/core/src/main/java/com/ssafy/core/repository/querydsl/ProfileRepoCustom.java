package com.ssafy.core.repository.querydsl;

import com.ssafy.core.dto.res.ChattingMemberResDto;
import com.ssafy.core.dto.res.MainProfileResDto;
import com.ssafy.core.dto.res.MissionResDto;
import com.ssafy.core.dto.res.SignInResDto;
import com.ssafy.core.entity.Profile;

import java.util.List;

public interface ProfileRepoCustom {

    Profile findProfileByUserPk(Long userPK);

    Long findProfileIdByUserPk(Long userPk);

    List<MainProfileResDto> findProfileListByFamilyId(Long familyId);

    SignInResDto findProfileIdAndFamilyIdByUserPk(Long userPk);

    Long checkRoleByFamilyIdExceptMe(Long familyId, String role, Long profileId);

    Long checkNicknameByFamilyIdExceptMe(Long familyId, String nickname, Long profileId);

    List<Profile> findProfilesByFamilyIdExceptMe(Long familyId, Long profileId);

    MissionResDto findTodayMissionByUserPk(Long userPk);

    List<ChattingMemberResDto> findChattingMemberListByFamilyId(Long familyId);
}
