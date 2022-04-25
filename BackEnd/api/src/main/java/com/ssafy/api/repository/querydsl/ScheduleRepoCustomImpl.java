package com.ssafy.api.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.api.dto.res.ScheduleDetailResDto;
import com.ssafy.api.entity.Family;
import com.ssafy.api.entity.QSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScheduleRepoCustomImpl implements ScheduleRepoCustom{

    private final JPAQueryFactory queryFactory;
    QSchedule schedule = QSchedule.schedule;
//    @Override
//    public List<ScheduleDetailResDto> findScheduleByMonth(String  month, Family family) {
//        return queryFactory
//                .select(Projections.fields(ScheduleDetailResDto.class,
//                        schedule.title,
//                        schedule.content,
//                        schedule.startDate,
//                        schedule.endDate,
//                        schedule.role))
//                .from(schedule)
//                .where(schedule.family.eq(family).and((schedule.startDate.month().toString() = month)
//                                .or(schedule.startDate.eq(month)))
//                                .and(schedule.endDate.after(month)
//                                        .or(schedule.endDate.eq(month)))))
//                .fetch();


//    }

    @Override
    public List<ScheduleDetailResDto> findScheduleByDay(LocalDate day, Family family) {
        System.out.println(day.getClass().getName());
        System.out.println(schedule.endDate.getClass().getName());
        return queryFactory
                .select(Projections.fields(ScheduleDetailResDto.class,
                        schedule.title,
                        schedule.content,
                        schedule.startDate,
                        schedule.endDate,
                        schedule.role))
                .from(schedule)
                .where(schedule.family.eq(family).and((schedule.startDate.before(day)
                                .or(schedule.startDate.eq(day)))
                                .and(schedule.endDate.after(day)
                                        .or(schedule.endDate.eq(day)))))
                .fetch();

    }
}
