package com.ssafy.api.controller;

import com.ssafy.api.config.jwt.JwtProvider;
import com.ssafy.api.dto.req.FamilyJoinDto;
import com.ssafy.api.dto.res.FamilyCodeResDto;
import com.ssafy.api.dto.res.FamilyIdResDto;
import com.ssafy.api.dto.res.FamilyPictureResDto;
import com.ssafy.api.entity.Family;
import com.ssafy.api.entity.User;
import com.ssafy.api.service.FamilyService;
import com.ssafy.api.service.UserService;
import com.ssafy.api.service.common.CommonResult;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.api.service.common.SingleResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/family")
@RequiredArgsConstructor
@Slf4j
public class FamilyController {

    private final FamilyService familyService;
    private final UserService userService;
    private final ResponseService responseService;
    private final JwtProvider jwtTokenProvider;

    @PostMapping("/create")
    @ApiImplicitParams({@ApiImplicitParam(name = "X-Auth-Token", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "가족 그룹 생성")
    public CommonResult createFamily(@RequestBody @Valid FamilyJoinDto familyRequest, HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        Long userPk = jwtTokenProvider.getUserPk(token);
        User user = userService.findByUserPk(userPk);
        userService.updateBirthday(userPk, familyRequest.getBirthday());
        Family family = familyService.createFamily();
        familyService.createProfile(family, user, familyRequest);
        return responseService.getSuccessResult("그룹 생성 완료");
    }

    @PostMapping("/join/{familyId}")
    @ApiImplicitParams({@ApiImplicitParam(name = "X-Auth-Token", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "가족 그룹 가입")
    public CommonResult joinFamily(
            @PathVariable long familyId,
            @RequestBody @Valid FamilyJoinDto familyRequest,
            HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        Long userPk = jwtTokenProvider.getUserPk(token);
        User user = userService.findByUserPk(userPk);
        userService.updateBirthday(userPk, familyRequest.getBirthday());
        Family family = familyService.getFamily(familyId);
        familyService.createProfile(family, user, familyRequest);
        return responseService.getSuccessResult("그룹 가입 완료");
    }

    @GetMapping("/code/check/{code}")
    @ApiImplicitParams({@ApiImplicitParam(name = "X-Auth-Token", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "가족 코드 검사", notes = "<strong>가족 코드<strong>를 받아 가족 id를 조회한다.")
    public SingleResult<FamilyIdResDto> checkFamilyCode(@PathVariable String code, HttpServletRequest request) {
        Family family = familyService.checkCode(code);
        FamilyIdResDto res = FamilyIdResDto.builder()
                .id(family.getId())
                .build();
        return responseService.getSingleResult(res);
    }

    @GetMapping("/code")
    @ApiImplicitParams({@ApiImplicitParam(name = "X-Auth-Token", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "가족 코드 조회", notes = "<strong>가족 id<strong>를 받아 가족 코드를 조회한다.")
    public SingleResult<FamilyCodeResDto> getFamilyCode(HttpServletRequest request) {
        long familyId = familyService.fromUserIdToFamilyId(request);
        Family family = familyService.getFamily(familyId);
        FamilyCodeResDto res = FamilyCodeResDto.builder()
                .code(family.getCode())
                .build();
        return responseService.getSingleResult(res);
    }

    @PutMapping("/picture")
    @ApiImplicitParams({@ApiImplicitParam(name = "X-Auth-Token", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "가족 사진 수정", notes = "<strong>가족 사진<stong>을 추가 및 수정한다.")
    public CommonResult updateFamilyPicture(
            @RequestParam(value = "picture", required = true) MultipartFile picture, HttpServletRequest request) {
        long familyId = familyService.fromUserIdToFamilyId(request);
        String path = request.getServletContext().getRealPath("");
        familyService.updateFamilyPicture(familyId, picture, path);
        return responseService.getSuccessResult();
    }

    @GetMapping("/picture")
    @ApiImplicitParams({@ApiImplicitParam(name = "X-Auth-Token", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "가족 사진 경로 조회", notes = "<strong>가족 사진 경로<stong>를 조회한다.")
    public SingleResult<FamilyPictureResDto> getFamilyPicture(HttpServletRequest request) {
        long familyId = familyService.fromUserIdToFamilyId(request);
        Family family = familyService.getFamilyPicture(familyId);
        String picture = family.getPicture();
        FamilyPictureResDto res = FamilyPictureResDto.builder()
                .picture(picture)
                .build();
        return responseService.getSingleResult(res);
    }

    ;

}
