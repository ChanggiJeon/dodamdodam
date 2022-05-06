package com.ssafy.core.repository.querydsl;

import com.ssafy.core.dto.req.FindIdReqDto;
import com.ssafy.core.entity.Profile;

public interface UserRepoCustom {

    String findUserIdByUserInfo(FindIdReqDto request);

    String findUserFcmTokenByProfile(Profile target);
}
