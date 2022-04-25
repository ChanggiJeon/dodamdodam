package com.ssafy.api.controller;


import com.ssafy.api.config.jwt.JwtProvider;
import com.ssafy.api.dto.req.NewScheduleDto;
import com.ssafy.api.dto.res.ScheduleDetailResDto;
import com.ssafy.api.entity.Family;
import com.ssafy.api.entity.Schedule;
import com.ssafy.api.entity.User;
import com.ssafy.api.service.FamilyService;
import com.ssafy.api.service.ScheduleService;
import com.ssafy.api.service.UserService;
import com.ssafy.api.service.common.CommonResult;
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
    public CommonResult createSchedule (@RequestBody @Valid NewScheduleDto scheduleReq, HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        Long userPk = jwtTokenProvider.getUserPk(token);
        User user = userService.findByUserPk(userPk);
        Family family = familyService.fromUserIdToFamilyId(request);
        scheduleService.createSchedule(scheduleReq, family, user);
        return responseService.getSuccessResult("일정 생성 완료");
    }

    @GetMapping("/{scheduleId}")
    @ApiImplicitParams({@ApiImplicitParam(name = "X-Auth-Token", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public SingleResult<ScheduleDetailResDto> scheduleDetail(@PathVariable long scheduleId, HttpServletRequest request) {
        Family family = familyService.fromUserIdToFamilyId(request);
        Schedule schedule = scheduleService.getSchedule(scheduleId);
        ScheduleDetailResDto res = ScheduleDetailResDto.builder()
                .id(scheduleId)
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .startDate(schedule.getStartDate().toString())
                .endDate(schedule.getEndDate().toString())
                .role(schedule.getRole())
                .build();
        return responseService.getSingleResult(res);

    }

    @PatchMapping("/{scheduleId}")
    @ApiImplicitParams({@ApiImplicitParam(name = "X-Auth-Token", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public CommonResult updateSchedule(@PathVariable long scheduleId,
                                       NewScheduleDto scheduleReq,
                                       HttpServletRequest request) {
        Family family = familyService.fromUserIdToFamilyId(request);
        Schedule schedule = scheduleService.getSchedule(scheduleId);
        scheduleService.updateSchedule(schedule, scheduleReq);
        return responseService.getSuccessResult("일정 수정 완료");
    }

}
