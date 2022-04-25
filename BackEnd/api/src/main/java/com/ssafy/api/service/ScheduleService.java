package com.ssafy.api.service;

import com.ssafy.api.dto.req.NewScheduleDto;
import com.ssafy.api.entity.Family;
import com.ssafy.api.entity.Profile;
import com.ssafy.api.entity.Schedule;
import com.ssafy.api.entity.User;
import com.ssafy.api.exception.CustomException;
import com.ssafy.api.repository.ProfileRepository;
import com.ssafy.api.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;

import static com.ssafy.api.exception.CustomErrorCode.INVALID_REQUEST;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ProfileRepository profileRepository;

    public void createSchedule(NewScheduleDto scheduleReq, Family family, User user) {
        Profile profile = profileRepository.findProfileByUserPk(user.getUserPk());
        LocalDate startDate = stringToLocalDate(scheduleReq.getStartDate());
        LocalDate endDate;
        if (scheduleReq.getEndDate().length() > 0) {
            endDate = stringToLocalDate(scheduleReq.getEndDate());
        } else {
            endDate = startDate;
        }
        scheduleRepository.save(Schedule.builder()
                .title(scheduleReq.getTitle())
                .content(scheduleReq.getContent())
                .startDate(startDate)
                .endDate(endDate)
                .family(family)
                .role(profile.getRole())
                .build());
    }

    public LocalDate stringToLocalDate(String localDate) {
        String[] localDateList = localDate.split("-");
        LocalDate result;
        try {
            if (localDateList[0].length() != 4 || localDateList[1].length() != 2 || localDateList[2].length() != 2) {
                throw new CustomException(INVALID_REQUEST, "잘못된 날짜 입력 방식입니다.");
            }
            result = LocalDate.of(
                    Integer.parseInt(localDateList[0]),
                    Integer.parseInt(localDateList[1]),
                    Integer.parseInt(localDateList[2]));
        } catch (NumberFormatException | DateTimeException e) {
            throw new CustomException(INVALID_REQUEST);
        }
        return result;
    }

    public Schedule getSchedule (long scheduleId) {
        Schedule schedule = scheduleRepository.findScheduleById(scheduleId);
        if (schedule == null) {
            throw new CustomException(INVALID_REQUEST, "해당 일정이 없습니다.");
        }
        return scheduleRepository.findScheduleById(scheduleId);
    }

    public void updateSchedule(Schedule schedule, NewScheduleDto scheduleReq) {
        LocalDate startDate = stringToLocalDate(scheduleReq.getStartDate());
        LocalDate endDate;
        if (scheduleReq.getEndDate().length() > 0) {
            endDate = stringToLocalDate(scheduleReq.getEndDate());
        } else {
            endDate = startDate;
        }
        schedule.setTitle(scheduleReq.getTitle());
        schedule.setContent(scheduleReq.getContent());
        schedule.setStartDate(startDate);
        schedule.setEndDate(endDate);
        scheduleRepository.save(schedule);
    }
}
