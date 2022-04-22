package com.ssafy.api.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.api.dto.res.MainProfileResDto;
import com.ssafy.api.entity.Profile;
import com.ssafy.api.entity.QFamily;
import com.ssafy.api.entity.QProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProfileRepoCustomImpl implements ProfileRepoCustom {
    private final JPAQueryFactory queryFactory;

    QProfile profile = QProfile.profile;
    QFamily family = QFamily.family;

    @Override
    public Profile findProfileById(String userId) {
        return queryFactory
                .selectFrom(profile)
                .where(profile.user.userId.eq(userId))
                .fetchFirst();
    }

    @Override
    public List<MainProfileResDto> getProfileListByFamilyId(Long familyId) {
        return queryFactory
                .select(Projections.fields(MainProfileResDto.class,
                        profile.imagePath,
                        profile.role,
                        profile.emotion,
                        profile.comment,
                        profile.user.id.as("userPk")))
                .from(profile)
                .where(profile.family.id.eq(familyId))
                .fetch();

    }

}
