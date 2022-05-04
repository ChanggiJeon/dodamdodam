package com.ssafy.core.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.core.dto.req.FindIdReqDto;
import com.ssafy.core.entity.QProfile;
import com.ssafy.core.entity.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QUser user = QUser.user;
    QProfile profile = QProfile.profile;

    @Override
    public String findUserIdByUserInfo(FindIdReqDto request) {

        return jpaQueryFactory.select(user.userId)
                .from(user)
                .join(profile)
                .on(user.userPk.eq(profile.user.userPk))
                .where(user.name.eq(request.getName())
                        .and(user.birthday.eq(request.getBirthday()))
                        .and(profile.family.code.eq(request.getFamilyCode())))
                .fetchFirst();
    }

    @Override
    public String findUserFcmTokenByProfileId(Long targetId) {
        return jpaQueryFactory.select(user.fcmToken)
                .from(user)
                .join(profile)
                .on(user.userPk.eq(profile.user.userPk))
                .where(profile.id.eq(targetId))
                .fetchFirst();
    }

    /**
     * QueryDsl 정리
     * 1. cross조인 해결법 -> join문 없이 그냥 where로 연관관계 직접 불러올 때 발생함.
     *  예시 ) from(profile).where(profile.family.code.eq(!@#!$))
     *  해결 ) from(profile).innerJoin(profile).where(profile.family.code.eq(!@#!$))
     *
     * 2. 연관관계가 없을 때 가져오는 법. (user에는 profile이 연결 x, or family에는 profile이 연결 x)
     *  위의 findUserByUserInfo처럼 .join(profile).on(user.id.eq(profile.user.id))처럼
     *  join(target) on(condition)으로 조건 직접 입력하여 조인 가능!
     *
     * 3. select에 entity로 검색하지 말고, responseDto로 넣어서 필요한 컬럼만 조회하자!
     *
     */

}

