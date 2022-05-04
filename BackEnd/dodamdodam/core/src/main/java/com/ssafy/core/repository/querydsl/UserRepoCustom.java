package com.ssafy.core.repository.querydsl;

import com.ssafy.core.dto.req.FindIdReqDto;

public interface UserRepoCustom {

    String findUserIdByUserInfo(FindIdReqDto request);

}
