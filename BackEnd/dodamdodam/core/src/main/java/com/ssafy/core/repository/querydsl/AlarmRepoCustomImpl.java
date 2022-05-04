package com.ssafy.core.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.core.entity.Alarm;
import com.ssafy.core.entity.Profile;
import com.ssafy.core.entity.QAlarm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AlarmRepoCustomImpl implements AlarmRepoCustom{

    private final JPAQueryFactory jpaQueryFactory;
    QAlarm alarm = QAlarm.alarm;

    @Override
    public Alarm findAlarmByProfileAndTarget(Profile me, Profile target) {
        return jpaQueryFactory.selectFrom(alarm)
                .where(alarm.me.eq(me).and(alarm.target.eq(target)))
                .fetchFirst();
    }
}
