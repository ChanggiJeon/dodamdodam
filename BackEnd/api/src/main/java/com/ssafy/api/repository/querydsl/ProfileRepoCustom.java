package com.ssafy.api.repository.querydsl;

import com.ssafy.api.dto.res.MainProfileResDto;
import com.ssafy.api.entity.Profile;

import java.util.List;

public interface ProfileRepoCustom {

    Profile findProfileById(String userId);

    List<MainProfileResDto> getProfileListByFamilyId(Long familyId);
}
