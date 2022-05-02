package com.ssafy.core.repository.querydsl;

import com.ssafy.core.dto.res.MainProfileResDto;
import com.ssafy.core.dto.res.SignInResDto;
import com.ssafy.core.entity.Profile;

import java.util.List;

public interface ProfileRepoCustom {

    Profile findProfileByUserPk(Long userPK);

    List<MainProfileResDto> getProfileListByFamilyId(Long familyId);

    SignInResDto findProfileIdAndFamilyIdByUserPk(Long userPk);

}
