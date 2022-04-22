package com.ssafy.api.controller;

import com.ssafy.api.common.Validate;
import com.ssafy.api.config.jwt.JwtProvider;
import com.ssafy.api.dto.req.FindIdReqDto;
import com.ssafy.api.dto.req.SignInReqDto;
import com.ssafy.api.dto.req.SignUpReqDto;
import com.ssafy.api.dto.req.UpdatePasswordReqDto;
import com.ssafy.api.dto.res.ReIssueTokenResDto;
import com.ssafy.api.dto.res.SignInResDto;
import com.ssafy.api.entity.User;
import com.ssafy.api.exception.CustomException;
import com.ssafy.api.service.UserService;
import com.ssafy.api.service.common.CommonResult;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.api.service.common.SingleResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.ssafy.api.exception.CustomErrorCode.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final ResponseService responseService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "{userId}")
    @ApiOperation(value = "ID 중복체크", notes = "<strong>아이디</strong>의 사용여부를 확인한다.")
    public CommonResult idCheck(@PathVariable String userId) {
        //공백과 특수문자가 안들어 있는상태에서 받았다고 치고 length만 유효성 검사함.
        idValidate(userId);

        userService.checkId(userId);

        return responseService.getSuccessResult("사용 가능한 아이디입니다.");
    }

    @PostMapping(value = "signup")
    @ApiOperation(value = "회원 가입", notes = "<strong>아이디, 패스워드, 이름</strong> 정보를 받아 회원가입 한다.")
    public CommonResult userSignUp
            (@RequestBody @Valid SignUpReqDto singUpRequest) {

        userService.userSignUp(singUpRequest);

        return responseService.getSuccessResult("회원가입 성공");
    }

    @PostMapping(value = "signin")
    @ApiOperation(value = "로그인", notes = "<strong>아이디, 패스워드</strong> 정보를 받아 로그인 한다.")
    public SingleResult<SignInResDto> userSignIn
            (@RequestBody @Valid SignInReqDto signInRequest) {

        User user = userService.findByUserId(signInRequest.getUserId());

        if (!passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())) {
            throw new CustomException(INVALID_REQUEST, "비밀번호를 잘못 입력하셨습니다.");
        }
        String token = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken();

        user.setRefreshToken(refreshToken);
        userService.saveUser(user);

        SignInResDto userInfo = SignInResDto.builder()
                .userId(user.getUserId())
                .birthday(user.getBirthday())
                .name(user.getName())
                .authority(user.getAuthority())
                .jwtToken(token)
                .refreshToken(refreshToken)
                .build();

        return responseService.getSingleResult(userInfo);
    }

    @PostMapping(value = "refresh")
    @ApiImplicitParams({@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "JWT 토큰 재발급", notes = "<strong>Refresh Token<strong>으로 AccessToken을 재발급 받는다.")
    public SingleResult<ReIssueTokenResDto> reissueAccessToken(@RequestHeader(value = "X-AUTH-TOKEN") String refreshToken) {

        //refreshToken 만료기간 확인
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new CustomException(INVALID_TOKEN);
        }

        User user = userService.findUserByRefreshToken(refreshToken);

        String token = jwtProvider.createAccessToken(user);
        refreshToken = jwtProvider.createRefreshToken();

        user.setRefreshToken(refreshToken);
        userService.saveUser(user);

        ReIssueTokenResDto res = ReIssueTokenResDto.builder()
                .jwtToken(token)
                .refreshToken(refreshToken)
                .build();

        return responseService.getSingleResult(res);
    }

    @PostMapping(value = "findId")
    @ApiOperation(value = "아이디 찾기", notes = "<strong>회원 정보<strong>로 아이디를 찾는다.")
    public CommonResult findUserIdWithUserInfo
            (@RequestBody @Valid FindIdReqDto request) {

        String userId = userService.findUserIdWithUserInfo(request);

        return responseService.getSuccessResult(userId);
    }

    @PostMapping(value = "newpassword")
    @ApiOperation(value = "비밀번호 재설정", notes = "<strong>비밀번호<strong>를 재설정한다.")
    public CommonResult updatePassword
            (@RequestBody @Valid UpdatePasswordReqDto request) {

        User user = userService.findByUserId(request.getUserId());

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(INVALID_REQUEST, "재설정 비밀번호가 기존 비밀번호와 같습니다!");
        }
        user.updatePassword(passwordEncoder.encode(request.getPassword()));
        userService.saveUser(user);

        return responseService.getSuccessResult();
    }

    @GetMapping(value = "birthday/{birthday}")
    @ApiOperation(value = "생일 정보 업데이트", notes = "<strong>생일<strong>정보를 업데이트 한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "X-Auth-Token", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public CommonResult updatePassword
            (@PathVariable String birthday, HttpServletRequest request) {

        String token = jwtProvider.resolveToken(request);
        String userId = jwtProvider.getUserId(token);
        log.info("userId: {}, birthday: {}", userId, birthday);
        userService.updateBirthday(userId, birthday);

        return responseService.getSuccessResult();
    }

    private void idValidate(String userId) {
        if (userId.length() < Validate.USER_ID_MIN.getNumber() ||
                userId.length() > Validate.USER_ID_MAX.getNumber()) {
            throw new CustomException(INVALID_REQUEST, "아이디는 4자 이상, 20자 이하여야 합니다.");
        }
    }
}
