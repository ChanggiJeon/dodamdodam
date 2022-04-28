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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
@Tag(name = "ScheduleController", description = "일정 컨트롤러")
public class ScheduleController {

    private final ResponseService responseService;
    private final JwtProvider jwtTokenProvider;
    private final ScheduleService scheduleService;
    private final FamilyService familyService;
    private final UserService userService;

    @Operation(summary = "일정 생성", description = "<strong>일정</strong>을 생성한다.",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResult createSchedule(@org.springframework.web.bind.annotation.RequestBody
                                       @io.swagger.v3.oas.annotations.parameters.RequestBody
                                       @Valid NewScheduleReqDto scheduleReq,
                                       HttpServletRequest request) {
        Long userPk = jwtTokenProvider.getUserPkFromRequest(request);
        User user = userService.findByUserPk(userPk);
        Family family = familyService.fromUserIdToFamily(request);
        scheduleService.createSchedule(scheduleReq, family, user);
        return responseService.getSuccessResult("일정 생성 완료");
    }

    @Operation(summary = "일정 상세 정보", description = "<strong>일정 상세 정보</strong>를 조회한다.",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @GetMapping(value = "/{scheduleId}", consumes = MediaType.APPLICATION_JSON_VALUE)
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

    @Operation(summary = "일정 수정", description = "<strong>일정</strong>을 수정한다.",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @PatchMapping(value = "/{scheduleId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResult updateSchedule(@PathVariable long scheduleId,
                                       NewScheduleReqDto scheduleReq,
                                       HttpServletRequest request) {
        Family family = familyService.fromUserIdToFamily(request);
        Schedule schedule = scheduleService.getSchedule(scheduleId, family);
        scheduleService.updateSchedule(schedule, scheduleReq);
        return responseService.getSuccessResult("일정 수정 완료");
    }

    @Operation(summary = "일정 삭제", description = "<strong>일정</strong>을 삭제한다.",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @DeleteMapping(value = "/{scheduleId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResult deleteSchedule(@PathVariable long scheduleId, HttpServletRequest request) {
        Family family = familyService.fromUserIdToFamily(request);
        Schedule schedule = scheduleService.getSchedule(scheduleId, family);
        scheduleService.deleteSchedule(schedule);
        return responseService.getSuccessResult("일정 삭제 완료");
    }

    @Operation(summary = "일자 일정 조회", description = "<strong>일자</strong>로 일정을 조회한다.",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @GetMapping(value = "/day/{day}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ListResult<ScheduleDetailResDto> scheduleListDay(@PathVariable String day, HttpServletRequest request) {
        Family family = familyService.fromUserIdToFamily(request);
        return responseService.getListResult(scheduleService.getScheduleListByDay(family, day));
    }

    @Operation(summary = "월별 일정 조회", description = "<strong>해당 월</strong>로 일정을 조회한다.",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @GetMapping(value = "/month/{month}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ListResult<ScheduleDetailResDto> scheduleListMonth(@PathVariable String month, HttpServletRequest request) {
        Family family = familyService.fromUserIdToFamily(request);
        return responseService.getListResult(scheduleService.getScheduleListByMonth(family, month));
    }
}
