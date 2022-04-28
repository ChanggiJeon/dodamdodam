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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/profile")
@Tag(name = "ProfileController", description = "프로필 컨트롤러")
public class ProfileController {
    private final ResponseService responseService;
    private final JwtProvider jwtProvider;
    private final ProfileService profileService;
    private final UserService userService;


    @Operation(summary = "프로필 등록", description = "<strong>프로필 등록</strong>",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResult enrollProfile(@org.springframework.web.bind.annotation.RequestBody
                                      @io.swagger.v3.oas.annotations.parameters.RequestBody
                                      @Valid ProfileReqDto profileRequest,
                                      @RequestParam(value = "file", required = false) MultipartFile multipartFile,
                                      HttpServletRequest request) {

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


    @Operation(summary = "프로필 수정", description = "<strong>프로필 수정</strong>",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @PatchMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResult updateProfile(@org.springframework.web.bind.annotation.RequestBody
                                      @io.swagger.v3.oas.annotations.parameters.RequestBody
                                      @Valid ProfileReqDto profileRequest,
                                      @RequestPart(value = "file", required = false) MultipartFile multipartFile, HttpServletRequest request) {

        Long userPk = jwtProvider.getUserPkFromRequest(request);

        Profile updateResult = profileService.updateProfile(userPk, profileRequest, multipartFile, request);

        userService.updateBirthdayWithUserPk(userPk, profileRequest.getBirthday());

        profileService.enrollProfile(updateResult);

        return responseService.getSuccessResult();
    }

//    @Operation(summary = "미션 등록", notes = "<strong>미션 등록</strong>",
//            parameters = {
//                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
//            })
//    @PatchMapping("/mission")
//    public CommonResult updateMission(@RequestBody @Valid MissionReqDto missionReqDto, HttpServletRequest request) {
//        String token = jwtTokenProvider.resolveToken(request);
//        String userId = jwtTokenProvider.getUserId(token);
//        Profile mission = profileService.enrollMission(userId, missionReqDto.getMissionContent());
//        profileService.enrollProfile(mission);
//
//        return responseService.getSuccessResult();
//    }


    @Operation(summary = "상태 수정", description = "<strong>상태 수정</strong>",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @PatchMapping(value = "/status", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResult updateStatus(@org.springframework.web.bind.annotation.RequestBody
                                     @io.swagger.v3.oas.annotations.parameters.RequestBody
                                     @Valid StatusReqDto statusReqDto, HttpServletRequest request) {

        Long userPk = jwtProvider.getUserPkFromRequest(request);

        Profile status = profileService.updateStatus(userPk, statusReqDto);

        profileService.enrollProfile(status);

        return responseService.getSuccessResult();
    }

    @Operation(summary = "프로필이미지 조회", description = "<strong>프로필 이미지 조회</strong>",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @GetMapping(value = "/image", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<String> getProfileImage(HttpServletRequest request) {

        Long userPk = jwtProvider.getUserPkFromRequest(request);

        return responseService.getSingleResult(profileService.findImage(userPk));
    }

}
