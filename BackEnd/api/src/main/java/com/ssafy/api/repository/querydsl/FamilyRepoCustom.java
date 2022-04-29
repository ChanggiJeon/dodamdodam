package com.ssafy.api.repository.querydsl;

import com.ssafy.api.entity.Family;

public interface FamilyRepoCustom {

    Family findFamilyByUserPk(Long userPk);

    Long findFamilyIdByUserPk(Long userPk);


}
