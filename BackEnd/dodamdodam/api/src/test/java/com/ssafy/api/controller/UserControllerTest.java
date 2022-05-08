package com.ssafy.api.controller;

import com.ssafy.api.ControllerTestSupport;
import com.ssafy.api.config.jwt.JwtProvider;
import com.ssafy.core.dto.req.FindIdReqDto;
import com.ssafy.core.dto.res.SignInResDto;
import com.ssafy.core.entity.User;
import com.ssafy.core.repository.ProfileRepository;
import com.ssafy.core.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends ControllerTestSupport {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ProfileRepository profileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private User user;

    private User makeDefaultUser() {
        return User.builder()
                .userId("test")
                .name("test")
                .password(passwordEncoder.encode("test123!"))
                .build();
    }

    @Test
    void idCheck_사용가능한_아이디() throws Exception {

        //given
        given(userRepository.existsUserByUserId(any(String.class))).willReturn(false);

        //when
        ResultActions result = mockMvc.perform(
                get("/api/user/{userId}", "test")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk());
    }

    @Test
    void idCheck_이미사용중인_아이디() throws Exception {

        //given
        given(userRepository.existsUserByUserId(any(String.class))).willReturn(true);

        //when
        ResultActions result = mockMvc.perform(
                get("/api/user/{userId}", "test")
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void idCheck_아이디_형식_틀림() throws Exception {

        //given
        given(userRepository.existsUserByUserId(any(String.class))).willReturn(false);

        //when
        ResultActions result = mockMvc.perform(
                get("/api/user/{userId}", "test!@#$")
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void userSignUp_정상입력() throws Exception {
        //given, when
        ResultActions result = mockMvc.perform(
                post("/api/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/user/signUp/success.json"))
        );

        //then
        result.andExpect(status().isOk());
    }

    @Test
    void userSignUp_한글_아이디_사용() throws Exception {
        //given, when
        ResultActions result = mockMvc.perform(
                post("/api/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/user/signUp/wrongId.json"))
        );
        //then
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void userSignUp_비밀번호_특수문자_입력안함() throws Exception {
        //given, when
        ResultActions result = mockMvc.perform(
                post("/api/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/user/signUp/wrongPassword.json"))
        );

        //then
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void userSignUp_이름_입력안함() throws Exception {
        //given, when
        ResultActions result = mockMvc.perform(
                post("/api/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/user/signUp/wrongName.json"))
        );

        //then
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void userSignIn_정상입력() throws Exception {
        //given, when
        given(userRepository.findUserByUserId(any(String.class)))
                .willReturn(Optional.of(makeDefaultUser()));
        given(profileRepository.findProfileIdAndFamilyIdByUserPk(any(Long.class)))
                .willReturn(SignInResDto.builder().familyId(1L).profileId(1L).build());

        ResultActions result = mockMvc.perform(
                post("/api/user/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/user/signIn/success.json"))
        );
        //then
        result.andExpect(status().isOk());
    }

    @Test
    void userSignIn_비밀번호_형식_틀림() throws Exception {
        //given, when
        given(userRepository.findUserByUserId(any(String.class)))
                .willReturn(Optional.of(makeDefaultUser()));
        given(profileRepository.findProfileIdAndFamilyIdByUserPk(any(Long.class)))
                .willReturn(new SignInResDto());

        ResultActions result = mockMvc.perform(
                post("/api/user/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/user/signIn/wrongPassword.json"))
        );
        //then
        result.andExpect(status().is4xxClientError());
    }


    //아이디와 비밀번호 잘못된 입력값을 찾아내는게 맞나?
    @Test
    void userSignIn_아이디_공백() throws Exception {
        //given, when
        given(userRepository.findUserByUserId(any(String.class)))
                .willReturn(Optional.of(makeDefaultUser()));
        given(profileRepository.findProfileIdAndFamilyIdByUserPk(any(Long.class)))
                .willReturn(new SignInResDto());

        ResultActions result = mockMvc.perform(
                post("/api/user/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/user/signIn/blankId.json"))
        );
        //then
        result.andExpect(status().is4xxClientError());
    }


    @Test
    @WithMockUser
    void reissueAccessToken_정상입력() throws Exception {
        //given
        given(jwtProvider.getUserPkFromExpiredToken(any(String.class)))
                .willReturn(1L);
        given(jwtProvider.validateToken(any(String.class))).willReturn(true);

        given(userRepository.findUserByUserPk(any(Long.class)))
                .willReturn(Optional.of(makeDefaultUser()));

        given(jwtProvider.createAccessToken(any(User.class)))
                .willReturn("access.token.123");

        given(jwtProvider.createRefreshToken())
                .willReturn("refresh.token.123");

        //when
        ResultActions result = mockMvc.perform(
                post("/api/user/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-AUTH-TOKEN", "token")
                        .header("X-AUTH-REFRESH-TOKEN", "refreshToken")
        );
        //then
        result.andExpect(status().isOk());
    }


    @Test
    void findUserIdWithUserInfo_정상입력() throws Exception {
        //given
        given(userRepository.findUserIdByUserInfo(any(FindIdReqDto.class)))
                .willReturn("test");

        //when
        ResultActions result = mockMvc.perform(
                post("/api/user/findId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/user/findId/success.json"))
        );
        //then
        result.andExpect(status().isOk());
    }

//    @Test
//    void findUserIdWithUserInfo_날짜_형식_틀림() throws Exception {
//        //given
//        given(userRepository.findUserIdByUserInfo(any(FindIdReqDto.class)))
//                .willReturn("test");
//
//        //when
//        ResultActions result = mockMvc.perform(
//                post("/api/user/findId")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(readJson("/user/findId/wrongDate.json"))
//        );
//        //then
//        result.andExpect(status().is4xxClientError());
//    }

    @Test
    void findUserIdWithUserInfo_가족코드_형식_틀림() throws Exception {
        //given
        given(userRepository.findUserIdByUserInfo(any(FindIdReqDto.class)))
                .willReturn("test");

        //when
        ResultActions result = mockMvc.perform(
                post("/api/user/findId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/user/findId/wrongFamilyCode.json"))
        );
        //then
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void updatePassword_정상입력() throws Exception {
        //given
        given(userRepository.findUserByUserId(any(String.class)))
                .willReturn(Optional.of(makeDefaultUser()));

        //when
        ResultActions result = mockMvc.perform(
                post("/api/user/newpassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/user/updatePassword/success.json"))
        );
        //then
        result.andExpect(status().isOk());
    }

    @Test
    void updatePassword_아이디_공백() throws Exception {
        //given
        given(userRepository.findUserByUserId(any(String.class)))
                .willReturn(Optional.of(makeDefaultUser()));

        //when
        ResultActions result = mockMvc.perform(
                post("/api/user/newpassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/user/updatePassword/blankId.json"))
        );
        //then
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void updatePassword_비밀번호_형식_틀림() throws Exception {
        //given
        given(userRepository.findUserByUserId(any(String.class)))
                .willReturn(Optional.of(makeDefaultUser()));

        //when
        ResultActions result = mockMvc.perform(
                post("/api/user/newpassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/user/updatePassword/wrongPassword.json"))
        );
        //then
        result.andExpect(status().is4xxClientError());
    }
//    @Test
//    void updateFcmToken() throws Exception {
//    }
}