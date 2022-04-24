package com.ssafy.api.controller;

import com.ssafy.api.config.jwt.JwtProvider;
import com.ssafy.api.dto.res.MainProfileResDto;
import com.ssafy.api.entity.User;
import com.ssafy.api.service.MainService;
import com.ssafy.api.service.common.CommonResult;
import com.ssafy.api.service.common.ListResult;
import com.ssafy.api.service.common.ResponseService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class MainController {

    private final MainService mainService;
    private final ResponseService responseService;
    private final JwtProvider jwtProvider;

    @GetMapping("{familyId}")
    @ApiImplicitParams({@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "가족 Profile 정보", notes = "본인을 제외한 가족의 <strong>profile<strong> 정보를 받는다.")
    public ListResult<MainProfileResDto> getProfileList(@PathVariable Long familyId, HttpServletRequest request) {
        Long userPk = jwtProvider.getUserPk(jwtProvider.resolveToken(request));
        return responseService.getListResult(mainService.getProfileListByFamilyId(familyId, userPk));
    }


//    @PostConstruct("suggestion")
//    @ApiImplicitParams({@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
//    @ApiOperation(value = "의견 제시 생성", notes = "<strong>의견 제시<strong>를 작성한다.")
//    public CommonResult createSuggestion(@RequestBody String text, HttpServletRequest request) {
//        User user = jwtProvider.resolveToken(request);
//        mainService.createSuggestion(text, userId);
//        return responseService.getSuccessResult("의견 제시가 정상적으로 등록되었습니다.");
//    }
}
