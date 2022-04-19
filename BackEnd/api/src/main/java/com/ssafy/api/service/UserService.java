package com.ssafy.api.service;

import com.ssafy.api.dto.user.FindIdDto;
import com.ssafy.api.dto.user.SignUpDto;
import com.ssafy.api.entity.User;
import com.ssafy.api.exception.CustomException;
import com.ssafy.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.Optional;

import static com.ssafy.api.exception.CustomErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User findByUserId(String userId) {
        User user = userRepository.findUserByUserId(userId).orElseThrow(() -> new CustomException(NO_SUCH_USER));
        return user;
    }

    @Transactional
    public void userSignUp(SignUpDto.Request userRequest) {

        if (userRepository.getByUserId(userRequest.getUserId()) != null) {
            throw new CustomException(DUPLICATE_USER_ID);
        }

        userRepository.save(User.builder()
                .userId(userRequest.getUserId())
                .name(userRequest.getName())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .authority("ROLE_USER")
                .build());
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public Optional<User> checkId(String userId) {
        return userRepository.findUserByUserId(userId);
    }

    public User findUserByRefreshToken(String refreshToken) {
        User user = userRepository.findUserByRefreshToken(refreshToken)
                .orElseThrow(() -> new CustomException(INVALID_TOKEN));
        return user;
    }

//    public void findUserIdWithUserInfo(FindIdDto.Request request) {
//        queryDSL 써야함!!
//    }
}
