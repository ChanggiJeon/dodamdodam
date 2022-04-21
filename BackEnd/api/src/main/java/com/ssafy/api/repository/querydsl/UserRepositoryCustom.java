package com.ssafy.api.repository.querydsl;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface UserRepositoryCustom {

    String findUserByUserInfo(String name, String familyCode, LocalDate birthday);
}
