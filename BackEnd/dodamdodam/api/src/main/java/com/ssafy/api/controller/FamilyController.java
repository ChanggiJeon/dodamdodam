package com.ssafy.api.controller;

import com.ssafy.core.dto.req.FamilyCreateReqDto;
import com.ssafy.core.dto.req.FamilyJoinReqDto;
import com.ssafy.core.dto.res.FamilyCodeResDto;
import com.ssafy.core.dto.res.FamilyIdResDto;
import com.ssafy.core.dto.res.FamilyPictureResDto;
import com.ssafy.core.entity.Family;
import com.ssafy.core.entity.Profile;
import com.ssafy.core.entity.User;
import com.ssafy.api.service.FamilyService;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/family")
@Tag(name = "FamilyController", description = "가족컨트롤러")
public class FamilyController {

    private final FamilyService familyService;
    private final UserService userService;
    private final ProfileService profileService;
    private final ResponseService responseService;

    @Operation(summary = "가족 그룹 생성", description = "<strong>가족 그룹</strong>을 생성한다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SingleResult<FamilyIdResDto> createFamily(
            @ModelAttribute @Valid FamilyCreateReqDto familyReq,
            Authentication authentication,
            HttpServletRequest request) {

        Long userPk = Long.parseLong(authentication.getName());
        User user = userService.findByUserPk(userPk);
        familyService.familyExistCheck(userPk);
        userService.updateBirthdayWithUserPk(userPk, familyReq.getBirthday());
        Family family = familyService.createFamily();
        String[] imageInfo = profileService.enrollImage(familyReq.getImage(), request).split("#");
        Profile profile = familyService.createProfileForFirst(family, user, familyReq, imageInfo);
        FamilyIdResDto res = FamilyIdResDto.builder()
                .familyId(family.getId())
                .profileId(profile.getId())
                .build();
        return responseService.getSingleResult(res);
    }

    @Operation(summary = "가족 그룹 가입", description = "<strong>가족 그룹<strong>을 가입한다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @PostMapping(value = "/join", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SingleResult<FamilyIdResDto> joinFamily(
            @ModelAttribute
            @Valid FamilyJoinReqDto familyRequest,
            Authentication authentication,
            HttpServletRequest request) {
        Long userPk = Long.parseLong(authentication.getName());
        User user = userService.findByUserPk(userPk);
        familyService.familyExistCheck(userPk);

        //Role 중복 검사
        profileService.checkRoleByFamilyIdExceptMe(familyRequest.getFamilyId(), familyRequest.getRole(), userPk);
        //NickName 중복 검사
        profileService.checkNicknameByFamilyIdExceptMe(familyRequest.getFamilyId(), familyRequest.getNickname(), userPk);

        userService.updateBirthdayWithUserPk(userPk, familyRequest.getBirthday());
        Family family = familyService.getFamily(familyRequest.getFamilyId());
        String[] imageInfo = profileService.enrollImage(familyRequest.getImage(), request).split("#");
        Profile profile = familyService.createProfileForJoin(family, user, familyRequest, imageInfo);
        FamilyIdResDto res = FamilyIdResDto.builder()
                .familyId(family.getId())
                .profileId(profile.getId())
                .build();
        return responseService.getSingleResult(res);
    }

    @Operation(summary = "가족 코드 검사", description = "<strong>가족 코드<strong>를 받아 가족 id를 조회한다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @GetMapping(value = "/code/check/{code}")
    public SingleResult<FamilyIdResDto> checkFamilyCode(@PathVariable String code) {
        Family family = familyService.checkCode(code);
        FamilyIdResDto res = FamilyIdResDto.builder()
                .familyId(family.getId())
                .build();
        return responseService.getSingleResult(res);
        //profile Id 반납 여부 생각해보기.
    }

    @Operation(summary = "가족 코드 조회", description = "<strong>가족 id<strong>를 받아 가족 코드를 조회한다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @GetMapping(value = "/code")
    public SingleResult<FamilyCodeResDto> getFamilyCode(Authentication authentication) {
        Long userPk = Long.parseLong(authentication.getName());
        Family family = familyService.fromUserIdToFamily(userPk);
        FamilyCodeResDto res = FamilyCodeResDto.builder()
                .code(family.getCode())
                .build();
        return responseService.getSingleResult(res);
    }

    @Operation(summary = "가족 사진 수정", description = "<strong>가족 사진<stong>을 추가 및 수정한다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @PatchMapping(value = "/picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResult updateFamilyPicture(
            @RequestParam(value = "picture") MultipartFile picture, Authentication authentication, HttpServletRequest request) {

        Long userPk = Long.parseLong(authentication.getName());
        Family family = familyService.fromUserIdToFamily(userPk);
        String path = request.getServletContext().getRealPath("");
        familyService.updateFamilyPicture(family, picture, path);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "가족 사진 경로 조회", description = "<strong>가족 사진 경로<stong>를 조회한다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @GetMapping(value = "/picture")
    public SingleResult<FamilyPictureResDto> getFamilyPicture(Authentication authentication) {
        Long userPk = Long.parseLong(authentication.getName());
        Family family = familyService.fromUserIdToFamily(userPk);
        String picture = family.getPicture();
        FamilyPictureResDto res = FamilyPictureResDto.builder()
                .picture(picture)
                .build();

        return responseService.getSingleResult(res);
    }
}
