package com.ssafy.api.controller;

import com.ssafy.api.dto.SignUpDto;
import com.ssafy.api.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("")
    @ApiOperation(value = "회원 가입", notes = "<strong>아이디와 패스워드</strong>를 통해 회원가입 한다.")
    public ResponseEntity userSignUp(@RequestBody @Valid SignUpDto.Request userRequest) {
        System.out.println(userRequest.toString());
        userService.userSignUp(userRequest);
        return new ResponseEntity("회원가입 성공", HttpStatus.OK);
    }

}
