package com.ssafy.api.repository;

import com.ssafy.api.entity.Schedule;
import com.ssafy.api.repository.querydsl.ScheduleRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepoCustom {
    Schedule findScheduleById(long scheduleId);
}
