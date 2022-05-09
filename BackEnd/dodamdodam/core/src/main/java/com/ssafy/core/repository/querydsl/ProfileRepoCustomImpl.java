package com.ssafy.core.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.core.dto.res.ChattingMemberResDto;
import com.ssafy.core.dto.res.MainProfileResDto;
import com.ssafy.core.dto.res.MissionResDto;
import com.ssafy.core.dto.res.SignInResDto;
import com.ssafy.core.entity.Profile;
import com.ssafy.core.entity.QProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProfileRepoCustomImpl implements ProfileRepoCustom {
    private final JPAQueryFactory queryFactory;

    QProfile profile = QProfile.profile;

    @Override
    public Profile findProfileByUserPk(Long userPK) {
        return queryFactory
                .selectFrom(profile)
                .where(profile.user.userPk.eq(userPK))
                .fetchFirst();
    }

    @Override
    public Long findProfileIdByUserPk(Long userPk) {
        return queryFactory
                .select(profile.id)
                .from(profile)
                .where(profile.user.userPk.eq(userPk))
                .fetchFirst();
    }

    @Override
    public List<MainProfileResDto> findProfileListByFamilyId(Long familyId) {
        return queryFactory
                .select(Projections.fields(MainProfileResDto.class,
                        profile.imagePath,
                        profile.role,
                        profile.emotion,
                        profile.comment,
                        profile.user.userPk))
                .from(profile)
                .where(profile.family.id.eq(familyId))
                .fetch();
    }

    @Override
    public SignInResDto findProfileIdAndFamilyIdByUserPk(Long userPk) {
        return queryFactory
                .select(Projections.fields(SignInResDto.class,
                        profile.id.as("profileId"),
                        profile.family.id.as("familyId")))
                .from(profile)
                .where(profile.user.userPk.eq(userPk))
                .fetchFirst();
    }

    @Override
    public Long checkRoleByFamilyIdExceptMe(Long familyId, String role, Long profileId) {
        return queryFactory.select(profile.id.count())
                .from(profile)
                .where(profile.family.id.eq(familyId).and(profile.role.eq(role))
                        , profileIdNotEquals(profileId))
                .fetchFirst();
    }

    @Override
    public Long checkNicknameByFamilyIdExceptMe(Long familyId, String nickname, Long profileId) {
        return queryFactory.select(profile.id.count())
                .from(profile)
                .where(profile.family.id.eq(familyId).and(profile.nickname.eq(nickname))
                        , profileIdNotEquals(profileId))
                .fetchFirst();
    }

    @Override
    public List<Profile> findProfilesByFamilyIdExceptMe(Long familyId, Long profileId) {
        return queryFactory.select(profile)
                .from(profile)
                .where(profile.family.id.eq(familyId)
                        .and(profile.id.ne(profileId)))
                .fetch();
    }

    @Override
    public MissionResDto findTodayMissionByUserPk(Long userPk) {
        return queryFactory.select(Projections.fields(MissionResDto.class,
                        profile.mission_content.as("missionContent"),
                        profile.mission_target.as("missionTarget")))
                .from(profile)
                .where(profile.user.userPk.eq(userPk))
                .fetchFirst();
    }

    @Override
    public List<ChattingMemberResDto> findChattingMemberListByFamilyId(Long familyId) {
        List<Long> ids = queryFactory
                .select(profile.id)
                .from(profile)
                .where(profile.family.id.eq(familyId))
                .fetch();

        return queryFactory
                .select(Projections.fields(ChattingMemberResDto.class,
                                profile.id.as("profileId"),
                                profile.imagePath.as("profileImage"),
                                profile.nickname))
                .from(profile)
                .where(profile.id.in(ids))
                .fetch();
    }

    private BooleanExpression profileIdNotEquals(Long profileId) {
        return profileId != null ? profile.id.ne(profileId) : null;
    }
}
