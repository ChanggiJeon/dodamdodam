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
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
    @ApiImplicitParams({@ApiImplicitParam(name = "X-Auth-Token", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public CommonResult createSchedule (@RequestBody @Valid NewScheduleReqDto scheduleReq, HttpServletRequest request) {
        Long userPk = jwtTokenProvider.getUserPkFromRequest(request);
        User user = userService.findByUserPk(userPk);
        Family family = familyService.fromUserIdToFamily(request);
        scheduleService.createSchedule(scheduleReq, family, user);
        return responseService.getSuccessResult("일정 생성 완료");
    }

    @GetMapping("/{scheduleId}")
    @ApiImplicitParams({@ApiImplicitParam(name = "X-Auth-Token", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
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
    @ApiImplicitParams({@ApiImplicitParam(name = "X-Auth-Token", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public CommonResult updateSchedule(@PathVariable long scheduleId,
                                       NewScheduleReqDto scheduleReq,
                                       HttpServletRequest request) {
        Family family = familyService.fromUserIdToFamily(request);
        Schedule schedule = scheduleService.getSchedule(scheduleId, family);
        scheduleService.updateSchedule(schedule, scheduleReq);
        return responseService.getSuccessResult("일정 수정 완료");
    }

    @DeleteMapping("/{scheduleId}")
    @ApiImplicitParams({@ApiImplicitParam(name = "X-Auth-Token", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public CommonResult deleteSchedule(@PathVariable long scheduleId, HttpServletRequest request) {
        Family family = familyService.fromUserIdToFamily(request);
        Schedule schedule = scheduleService.getSchedule(scheduleId, family);
        scheduleService.deleteSchedule(schedule);
        return responseService.getSuccessResult("일정 삭제 완료");
    }

    @GetMapping("/day/{day}")
    @ApiImplicitParams({@ApiImplicitParam(name = "X-Auth-Token", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ListResult<ScheduleDetailResDto> scheduleListDay(@PathVariable String day, HttpServletRequest request) {
        Family family = familyService.fromUserIdToFamily(request);
        return responseService.getListResult(scheduleService.getScheduleListByDay(family, day));
    }

    @GetMapping("/month/{month}")
    @ApiImplicitParams({@ApiImplicitParam(name = "X-Auth-Token", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ListResult<ScheduleDetailResDto> scheduleListMonth(@PathVariable String month, HttpServletRequest request) {
        Family family = familyService.fromUserIdToFamily(request);
        return responseService.getListResult(scheduleService.getScheduleListByMonth(family, month));
    }
}
