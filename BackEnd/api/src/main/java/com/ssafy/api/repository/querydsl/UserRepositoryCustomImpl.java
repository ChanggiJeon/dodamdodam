package com.ssafy.api.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
//import com.ssafy.api.entity.QProfile;
//import com.ssafy.api.entity.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Repository
@Transactional
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QUser user = QUser.user;
    QProfile profile = QProfile.profile;

    @Override
    public String findUserByUserInfo(String name, String familyCode, LocalDate birthday) {
        return null;
        return jpaQueryFactory.select(user.userId)
                .from(user)
                .where(user.name.eq(name).and(user.birthday.eq(birthday)))
                .innerJoin(user, profile.user)
                .where(profile.family.code.eq(familyCode))
                .fetchFirst();
    }
}
