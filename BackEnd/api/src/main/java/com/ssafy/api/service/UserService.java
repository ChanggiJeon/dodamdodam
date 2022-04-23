package com.ssafy.api.service;

import com.ssafy.api.config.jwt.JwtProvider;
import com.ssafy.api.dto.req.FindIdReqDto;
import com.ssafy.api.dto.req.SignUpReqDto;
import com.ssafy.api.entity.User;
import com.ssafy.api.exception.CustomException;
import com.ssafy.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import java.time.DateTimeException;
import java.time.LocalDate;

import static com.ssafy.api.exception.CustomErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public User findByUserId(String userId) {
        return userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new CustomException(NO_SUCH_USER));
    }

    public User findByUserPk(Long userPk){
        return userRepository.findUserByUserPk(userPk)
                .orElseThrow(() -> new CustomException(NO_SUCH_USER));
    }

    @Transactional()
    public void userSignUp(SignUpReqDto singUpRequest) {

        if (userRepository.getByUserId(singUpRequest.getUserId()) != null) {
            throw new CustomException(DUPLICATE_USER_ID);
        }

        userRepository.save(User.builder()
                .userId(singUpRequest.getUserId())
                .name(singUpRequest.getName())
                .password(passwordEncoder.encode(singUpRequest.getPassword()))
                .authority("ROLE_USER")
                .build());
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void checkId(String userId) {
        if(userRepository.findUserByUserId(userId).isPresent()){
            throw new CustomException(DUPLICATE_USER_ID);
        }
    }

    public String findUserIdWithUserInfo(FindIdReqDto request) {
        return userRepository.findUserIdByUserInfo(request);
    }

    public void updateBirthdayWithUserPk(Long userPk, String birthday) {

        User user = this.findByUserPk(userPk);

        String[] list = birthday.split("-");

        try {
            if (list[0].length() != 4 || list[1].length() != 2 || list[2].length() != 2) {
                throw new CustomException(INVALID_REQUEST);
            }
            user.setBirthday(LocalDate.of(Integer.parseInt(list[0]), Integer.parseInt(list[1]), Integer.parseInt(list[2])));
        } catch (NumberFormatException | DateTimeException e) {
            throw new CustomException(INVALID_REQUEST);
        }

        userRepository.save(user);
    }

    public User getUserFromRequest(HttpServletRequest request){
        return this.findByUserPk(jwtProvider.getUserPk(jwtProvider.resolveToken(request)));
    }

    public Long getUserPkFromRequest(HttpServletRequest request){
        return jwtProvider.getUserPk(jwtProvider.resolveToken(request));
    }
}
