package com.ssafy.api.controller;


import com.ssafy.api.config.jwt.JwtProvider;
import com.ssafy.api.dto.req.ProfileReqDto;
import com.ssafy.api.dto.req.StatusReqDto;
import com.ssafy.api.entity.Profile;
import com.ssafy.api.entity.User;
import com.ssafy.api.service.ProfileService;
import com.ssafy.api.service.UserService;
import com.ssafy.api.service.common.CommonResult;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.api.service.common.SingleResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api(tags = {"프로필"})
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/profile")
public class ProfileController {
    private final ResponseService responseService;
    private final JwtProvider jwtProvider;
    private final ProfileService profileService;
    private final UserService userService;


    @ApiImplicitParams({@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "프로필 등록", notes = "<strong>프로필 등록</strong>")
    public CommonResult enrollProfile(@ModelAttribute @Valid ProfileReqDto profileRequest,
                                      @RequestPart(value = "file", required = false) MultipartFile multipartFile, HttpServletRequest request) {

        User user = jwtProvider.getUserFromRequest(request);

        userService.updateBirthdayWithUserPk(user.getUserPk(), profileRequest.getBirthday());

        String[] imageInfo = profileService.enrollImage(multipartFile, request).split("#");
        //family코드로 넣을 부분 필요
        Profile profile = Profile.builder()
                .role(profileRequest.getRole())
                .nickname(profileRequest.getNickname())
                .user(user)
                .imagePath(imageInfo[0])
                .imageName(imageInfo[1])
//                .family()
                .build();

        profileService.enrollProfile(profile);
        return responseService.getSuccessResult();
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @PatchMapping("")
    @ApiOperation(value = "프로필 수정", notes = "<strong>프로필 수정</strong>")
    public CommonResult updateProfile(@ModelAttribute @Valid ProfileReqDto profileRequest,
                                      @RequestPart(value = "file", required = false) MultipartFile multipartFile, HttpServletRequest request) {

        Long userPk = jwtProvider.getUserPkFromRequest(request);

        Profile updateResult = profileService.updateProfile(userPk, profileRequest, multipartFile, request);

        userService.updateBirthdayWithUserPk(userPk, profileRequest.getBirthday());

        profileService.enrollProfile(updateResult);

        return responseService.getSuccessResult();
    }

//    @ApiImplicitParams({@ApiImplicitParam(name = "X-Auth-Token", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
//    @PatchMapping("/mission")
//    @ApiOperation(value = "미션 등록", notes = "<strong>미션 등록</strong>")
//    public CommonResult updateMission(@RequestBody @Valid MissionReqDto missionReqDto, HttpServletRequest request) {
//        String token = jwtTokenProvider.resolveToken(request);
//        String userId = jwtTokenProvider.getUserId(token);
//        Profile mission = profileService.enrollMission(userId, missionReqDto.getMissionContent());
//        profileService.enrollProfile(mission);
//
//        return responseService.getSuccessResult();
//    }


    @ApiImplicitParams({@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @PatchMapping("/status")
    @ApiOperation(value = "상태 수정", notes = "<strong>상태 수정</strong>")
    public CommonResult updateStatus(@RequestBody @Valid StatusReqDto statusReqDto, HttpServletRequest request) {

        Long userPk = jwtProvider.getUserPkFromRequest(request);

        Profile status = profileService.updateStatus(userPk, statusReqDto);

        profileService.enrollProfile(status);

        return responseService.getSuccessResult();
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @GetMapping("/image")
    @ApiOperation(value = "프로필이미지 조회", notes = "<strong>프로필 이미지 조회</strong>")
    public SingleResult<String> getProfileImage(HttpServletRequest request) {

        Long userPk = jwtProvider.getUserPkFromRequest(request);

        return responseService.getSingleResult(profileService.findImage(userPk));
    }

}
