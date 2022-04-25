package com.ssafy.api.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.api.entity.Family;
import com.ssafy.api.entity.QFamily;
import com.ssafy.api.entity.QProfile;
import com.ssafy.api.entity.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FamilyRepoCustomImpl implements FamilyRepoCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QUser user = QUser.user;
    QProfile profile = QProfile.profile;
    QFamily family = QFamily.family;

    @Override
    public Family findFamilyByUserPk(Long userPk) {
        return jpaQueryFactory.select(family)
                .from(family)
                .join(profile)
                .on(family.id.eq(profile.family.id))
                .join(user)
                .on(profile.user.userPk.eq(user.userPk))
                .where(user.userPk.eq(userPk))
                .fetchFirst();
    }
}
