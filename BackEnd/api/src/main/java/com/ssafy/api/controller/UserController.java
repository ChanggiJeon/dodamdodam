package com.ssafy.api.controller;

import com.ssafy.api.config.jwt.JwtProvider;
import com.ssafy.api.dto.user.*;
import com.ssafy.api.entity.User;
import com.ssafy.api.exception.CustomErrorCode;
import com.ssafy.api.exception.CustomException;
import com.ssafy.api.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.ssafy.api.exception.CustomErrorCode.INVALID_REQUEST;
import static com.ssafy.api.exception.CustomErrorCode.INVALID_TOKEN;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "ID 중복체크", notes = "<strong>아이디</strong>의 사용여부를 확인한다.")
    public ResponseEntity<?> idCheck(@PathVariable String userId) {
        userService.checkId(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "회원 가입", notes = "<strong>아이디, 패스워드, 이름</strong> 정보를 받아 회원가입 한다.")
    public ResponseEntity<String> userSignUp
            (@RequestBody @Valid SignUpDto.Request singUpRequest) {

        userService.userSignUp(singUpRequest);

        return new ResponseEntity<>("회원가입 성공", HttpStatus.OK);
    }

    @PostMapping(value = "singin", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "로그인", notes = "<strong>아이디, 패스워드</strong> 정보를 받아 로그인 한다.")
    public ResponseEntity<SignInDto.Response> userSignUp
            (@RequestBody @Valid SignInDto.Request signInRequest) {

        User user = userService.findByUserId(signInRequest.getUserId());

        if (user == null) {
            throw new CustomException(INVALID_REQUEST, "아이디를 잚못 입력하셨습니다.");
        } else if (passwordEncoder.matches(user.getPassword(), signInRequest.getPassword())) {
            throw new CustomException(INVALID_REQUEST, "비밀번호를 잘못 입력하셨습니다.");
        }
        String token = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken();

        user.setRefreshToken(refreshToken);
        userService.saveUser(user);

        SignInDto.Response userInfo = SignInDto.Response.builder()
                .userId(user.getUserId())
                .birthday(user.getBirthday())
                .name(user.getName())
                .authority(user.getAuthority())
                .jwtToken(token)
                .refreshToken(refreshToken)
                .build();

        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    @PostMapping(value = "refresh", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "JWT 토큰 재발급", notes = "<strong>Refresh Token<strong>으로 AccessToken을 재발급 받는다.")
    public ResponseEntity<ReIssueTokenDto.Response> reissueAccessToken
            (@RequestBody @Valid HttpServletRequest request) {

        String refreshToken = request.getHeader("X-Auth-Token");

        //refreshToken 만료기간 확인
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new CustomException(INVALID_TOKEN);
        }

        User user = userService.findUserByRefreshToken(refreshToken);

        String token = jwtProvider.createAccessToken(user);
        refreshToken = jwtProvider.createRefreshToken();

        user.setRefreshToken(refreshToken);
        userService.saveUser(user);

        ReIssueTokenDto.Response res = ReIssueTokenDto.Response.builder()
                .jwtToken(token)
                .refreshToken(refreshToken)
                .build();

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

//    @PostMapping(value = "findId", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "아이디 찾기", notes = "<strong>회원 정보<strong>로 아이디를 찾는다.")
//    public ResponseEntity<FindIdDto.Response> findUserIdWithUserInfo
//            (@RequestBody @Valid FindIdDto.Request request) {
//
//        userService.findUserIdWithUserInfo(request);
//
//        //refreshToken 만료기간 확인
//        if (!jwtProvider.validateToken(refreshToken)) {
//            throw new CustomException(INVALID_TOKEN);
//        }
//
//        User user = userService.findUserByRefreshToken(refreshToken);
//
//        String token = jwtProvider.createAccessToken(user);
//        refreshToken = jwtProvider.createRefreshToken();
//
//        user.setRefreshToken(refreshToken);
//        userService.saveUser(user);
//
//        ReIssueTokenDto.Response res = ReIssueTokenDto.Response.builder()
//                .jwtToken(token)
//                .refreshToken(refreshToken)
//                .build();
//
//        return new ResponseEntity<>(res, HttpStatus.OK);
//    }

    @PostMapping(value = "newpassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "비밀번호 재설정", notes = "<strong>비밀번호<strong>를 재설정한다.")
    public ResponseEntity<String> updatePassword
            (@RequestBody @Valid updatePasswordDto.Request request) {

        User user = userService.findByUserId(request.getUserId());

        if(passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new CustomException(INVALID_REQUEST,"재설정 비밀번호가 기존 비밀번호와 같습니다!");
        }
        user.updatePassword(passwordEncoder.encode(request.getPassword()));
        userService.saveUser(user);

        return new ResponseEntity<>("성공!", HttpStatus.OK);
    }
}

