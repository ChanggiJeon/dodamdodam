package com.ssafy.api.controller;


import com.ssafy.api.config.jwt.JwtProvider;
import com.ssafy.api.dto.req.NewScheduleReqDto;
import com.ssafy.api.dto.res.ScheduleDetailResDto;
import com.ssafy.api.entity.Family;
import com.ssafy.api.entity.Schedule;
import com.ssafy.api.entity.User;
import com.ssafy.api.service.FamilyService;
import com.ssafy.api.service.ScheduleService;
import com.ssafy.api.service.UserService;
import com.ssafy.api.service.common.CommonResult;
import com.ssafy.api.service.common.ListResult;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.api.service.common.SingleResult;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {

    private final ResponseService responseService;
    private final JwtProvider jwtTokenProvider;
    private final ScheduleService scheduleService;
    private final FamilyService familyService;
    private final UserService userService;

    @PostMapping("/create")
    @Parameters({@Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)})
    public CommonResult createSchedule (@RequestBody @Valid NewScheduleReqDto scheduleReq, HttpServletRequest request) {
        Long userPk = jwtTokenProvider.getUserPkFromRequest(request);
        User user = userService.findByUserPk(userPk);
        Family family = familyService.fromUserIdToFamily(request);
        scheduleService.createSchedule(scheduleReq, family, user);
        return responseService.getSuccessResult("일정 생성 완료");
    }

    @GetMapping("/{scheduleId}")
    @Parameters({@Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)})
    public SingleResult<ScheduleDetailResDto> scheduleDetail(@PathVariable long scheduleId, HttpServletRequest request) {
        Family family = familyService.fromUserIdToFamily(request);
        Schedule schedule = scheduleService.getSchedule(scheduleId, family);
        ScheduleDetailResDto res = ScheduleDetailResDto.builder()
                .scheduleId(scheduleId)
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .role(schedule.getRole())
                .build();
        return responseService.getSingleResult(res);

    }

    @PatchMapping("/{scheduleId}")
    @Parameters({@Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)})
    public CommonResult updateSchedule(@PathVariable long scheduleId,
                                       NewScheduleReqDto scheduleReq,
                                       HttpServletRequest request) {
        Family family = familyService.fromUserIdToFamily(request);
        Schedule schedule = scheduleService.getSchedule(scheduleId, family);
        scheduleService.updateSchedule(schedule, scheduleReq);
        return responseService.getSuccessResult("일정 수정 완료");
    }

    @DeleteMapping("/{scheduleId}")
    @Parameters({@Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)})
    public CommonResult deleteSchedule(@PathVariable long scheduleId, HttpServletRequest request) {
        Family family = familyService.fromUserIdToFamily(request);
        Schedule schedule = scheduleService.getSchedule(scheduleId, family);
        scheduleService.deleteSchedule(schedule);
        return responseService.getSuccessResult("일정 삭제 완료");
    }

    @GetMapping("/day/{day}")
    @Parameters({@Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)})
    public ListResult<ScheduleDetailResDto> scheduleListDay(@PathVariable String day, HttpServletRequest request) {
        Family family = familyService.fromUserIdToFamily(request);
        return responseService.getListResult(scheduleService.getScheduleListByDay(family, day));
    }

    @GetMapping("/month/{month}")
    @Parameters({@Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)})
    public ListResult<ScheduleDetailResDto> scheduleListMonth(@PathVariable String month, HttpServletRequest request) {
        Family family = familyService.fromUserIdToFamily(request);
        return responseService.getListResult(scheduleService.getScheduleListByMonth(family, month));
    }
}
