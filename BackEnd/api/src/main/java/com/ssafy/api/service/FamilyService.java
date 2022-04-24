package com.ssafy.api.service;

import com.ssafy.api.config.jwt.JwtProvider;
import com.ssafy.api.dto.req.FamilyJoinDto;
import com.ssafy.api.entity.Family;
import com.ssafy.api.entity.Profile;
import com.ssafy.api.entity.User;
import com.ssafy.api.exception.CustomException;
import com.ssafy.api.repository.FamilyRepository;
import com.ssafy.api.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Random;
import java.util.UUID;

import static com.ssafy.api.exception.CustomErrorCode.INVALID_REQUEST;

@Service
@Slf4j
@RequiredArgsConstructor
public class FamilyService {
    private final FamilyRepository familyRepository;
    private final ProfileRepository profileRepository;
    private final JwtProvider jwtTokenProvider;
    // 가족 생성 및 프로필 생성
    public Family createFamily() {
        String key;
        for (int i = 0; true; i++) {
            Random rnd = new Random();
            key = "";
            for (int j = 0; j < 15; j++) {
                if (rnd.nextBoolean()) {
                    key += ((char)((int)(rnd.nextInt(26)) + 65));
                } else {
                    key += (rnd.nextInt(10));
                }
            }
            if (familyRepository.findByCode(key) == null) {
                break;
            }
        }
        return familyRepository.save(Family.builder()
                .code(key)
                .build());
    }
    public void createProfile(Family family, User user, FamilyJoinDto familyRequest) {
        Profile profile = Profile.builder()
                .role(familyRequest.getRole())
                .nickname(familyRequest.getNickname())
                .user(user)
                .family(family)
                .build();
        profileRepository.save(profile);
    }

    public Family getFamily(long familyId) {
        Family family = familyRepository.findById(familyId);
        if (family == null) {
            throw new CustomException(INVALID_REQUEST, "해당 그룹이 없습니다.");
        }
        return family;
    }

    public Family checkCode(String code) {
        Family family = familyRepository.findByCode(code);
        if (family == null) {
            throw new CustomException(INVALID_REQUEST, "해당 그룹이 없습니다.");
        }
        return family;
    }


    public long fromUserIdToFamilyId(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        Long userPk = jwtTokenProvider.getUserPk(token);
        Profile profile = profileRepository.findProfileByUserPk(userPk);
        if (profile == null) {
            throw new CustomException(INVALID_REQUEST, "그룹에 권한이 없습니다.");
        }
        return profile.getFamily().getId();
    }

    public void updateFamilyPicture(long familyId, MultipartFile picture, String path) {
        String originFileName = picture.getOriginalFilename();
        UUID uuid = UUID.randomUUID();
        String saveFileName = "/resources/familyPicture/" + uuid.toString() + "_" + originFileName;
        File dir = new File(path + "/resources/familyPicture");
        Family family = familyRepository.findById(familyId);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        try{
            if(family.getPicture() != null) {
                File file = new File(path + family.getPicture());
                file.delete();
            }
            File file = new File(path + saveFileName);
            picture.transferTo(file);
        } catch (Exception e){
            throw new CustomException(INVALID_REQUEST, "파일이 없습니다.");
        }
        familyRepository.save(Family.builder()
                .picture(saveFileName)
                .build());
    }

    public Family getFamilyPicture(long familyId) {
        return familyRepository.findById(familyId);
    }
}
