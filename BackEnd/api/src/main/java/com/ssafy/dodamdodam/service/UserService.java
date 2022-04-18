package com.ssafy.dodamdodam.service;

import com.ssafy.dodamdodam.dto.request.UserRequest;
import com.ssafy.dodamdodam.entity.User;
import com.ssafy.dodamdodam.exception.CustomException;
import com.ssafy.dodamdodam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.ssafy.dodamdodam.exception.CustomErrorCode.DUPLICATE_USER_ID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void userSignUp(UserRequest.signUp userRequest) {

        if (userRepository.getByUserId(userRequest.getUserId()) != null) {
            throw new CustomException(DUPLICATE_USER_ID);
        }

        userRepository.save(User.builder()
                .userId(userRequest.getUserId())
                .name(userRequest.getName())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .build());
    }
}
