package com.ssafy.api.repository.querydsl;

import com.ssafy.api.dto.req.FindIdReqDto;

public interface UserRepositoryCustom {

    String findUserIdByUserInfo(FindIdReqDto request);
}
