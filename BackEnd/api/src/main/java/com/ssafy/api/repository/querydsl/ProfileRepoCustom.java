package com.ssafy.api.repository.querydsl;

import com.ssafy.api.dto.res.MainProfileResDto;
import com.ssafy.api.dto.res.SignInResDto;
import com.ssafy.api.entity.Profile;

import java.util.List;

public interface ProfileRepoCustom {

    Profile findProfileByUserPk(Long userPK);

    List<MainProfileResDto> getProfileListByFamilyId(Long familyId);

    SignInResDto findProfileIdAndFamilyIdByUserPk(Long userPk);

}
