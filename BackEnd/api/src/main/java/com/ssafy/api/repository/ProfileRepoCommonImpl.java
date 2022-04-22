package com.ssafy.api.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.api.entity.Profile;
import com.ssafy.api.entity.QProfile;

import javax.persistence.EntityManager;

public class ProfileRepoCommonImpl implements ProfileRepoCommon{
    private final JPAQueryFactory queryFactory;
    private EntityManager em;

    public ProfileRepoCommonImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }


    @Override
    public Profile findProfileById(String userId) {
        Profile result = queryFactory
                .selectFrom(QProfile.profile)
                .where(QProfile.profile.user.userId.eq(userId))
                .fetchFirst();
        return result;
    }

}
