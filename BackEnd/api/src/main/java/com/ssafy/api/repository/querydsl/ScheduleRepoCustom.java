package com.ssafy.api.repository.querydsl;

import com.ssafy.api.dto.res.ScheduleDetailResDto;
import com.ssafy.api.entity.Family;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepoCustom {
    List<ScheduleDetailResDto> findScheduleByMonth(Integer month, Family family);
    List<ScheduleDetailResDto> findScheduleByDay(LocalDate day, Family family);
}
