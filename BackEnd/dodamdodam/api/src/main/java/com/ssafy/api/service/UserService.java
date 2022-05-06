package com.ssafy.api.service;

import com.ssafy.core.dto.req.FcmTokenReqDto;
import com.ssafy.core.dto.req.FindIdReqDto;
import com.ssafy.core.dto.req.SignUpReqDto;
import com.ssafy.core.dto.res.SignInResDto;
import com.ssafy.core.common.Validate;
import com.ssafy.core.entity.User;
import com.ssafy.core.exception.CustomErrorCode;
import com.ssafy.core.exception.CustomException;
import com.ssafy.core.repository.FamilyRepository;
import com.ssafy.core.repository.ProfileRepository;
import com.ssafy.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.DateTimeException;
import java.time.LocalDate;

import static com.ssafy.core.exception.CustomErrorCode.NOT_FOUND_FAMILY;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    public User findByUserId(String userId) {
        return userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NO_SUCH_USER));
    }

    public User findByUserPk(Long userPk){
        return userRepository.findUserByUserPk(userPk)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NO_SUCH_USER));
    }

    @Transactional()
    public void userSignUp(SignUpReqDto singUpRequest) {

        if (userRepository.getByUserId(singUpRequest.getUserId()) != null) {
            throw new CustomException(CustomErrorCode.DUPLICATE_USER_ID);
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
            throw new CustomException(CustomErrorCode.DUPLICATE_USER_ID);
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
                throw new CustomException(CustomErrorCode.INVALID_REQUEST);
            }
            user.setBirthday(LocalDate.of(Integer.parseInt(list[0]), Integer.parseInt(list[1]), Integer.parseInt(list[2])));
        } catch (NumberFormatException | DateTimeException e) {
            throw new CustomException(CustomErrorCode.INVALID_REQUEST);
        }

        userRepository.save(user);
    }

    public SignInResDto findProfileIdAndFamilyId(Long userPk) {
        return profileRepository.findProfileIdAndFamilyIdByUserPk(userPk);
    }

    public void updateFcmToken(User user, FcmTokenReqDto fcmReq) {
        user.setFcmToken(fcmReq.getFcmToken());
        userRepository.save(user);
    }

    public boolean idValidate(String userId) {
        return userId.length() >= Validate.USER_ID_MIN.getNumber() &&
                userId.length() <= Validate.USER_ID_MAX.getNumber();
    }

    public Long getFamilyIdByUserPk(Long userPk) {
        Long familyId = familyRepository.findFamilyIdByUserPK(userPk);
        if(familyId == null){
            throw new CustomException(NOT_FOUND_FAMILY);
        }
        return familyId;
    }
}
