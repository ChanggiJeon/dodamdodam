package com.ssafy.api.repository.querydsl;

import com.ssafy.api.dto.req.FindIdReqDto;

import java.time.LocalDate;

public interface UserRepositoryCustom {

    String findUserByUserInfo(FindIdReqDto request);
}
