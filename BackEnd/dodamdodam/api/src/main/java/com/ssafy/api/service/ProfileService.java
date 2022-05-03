package com.ssafy.api.service;


import com.ssafy.core.dto.req.ProfileReqDto;
import com.ssafy.core.dto.req.StatusReqDto;
import com.ssafy.core.dto.res.MainProfileResDto;
import com.ssafy.core.entity.Family;
import com.ssafy.core.entity.Profile;
import com.ssafy.core.repository.FamilyRepository;
import com.ssafy.core.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserService userService;
    private final FamilyRepository familyRepository;
    private final Random random = new Random();

    @Transactional(readOnly = false)
    public void enrollProfile(Profile profile) {
        profileRepository.save(profile);
    }

    //    @Transactional(readOnly = false)
//    public void enrollProfile(String userId){
//        User user = userService.findByUserId(userId);
//
//        String[] imageInfo  = enrollImage(multipartFile, request).split("#");
//        //family코드로 넣을 부분 필요
//        Profile profile = Profile.builder()
//                .role(profileRequest.getRole())
//                .nickname(profileRequest.getNickname())
//                .user(user)
//                .imagePath(imageInfo[0])
//                .imageName(imageInfo[1])
////                .family()
//                .build();
//
//        profileService.enrollProfile(profile);
//
//    }
    @Transactional(readOnly = false)
    public Profile updateProfile(Long userPK, ProfileReqDto profileDto, MultipartFile multipartFile, HttpServletRequest request) {
        Profile profile = profileRepository.findProfileByUserPk(userPK);
        //이미지 originalName, path 수정
//        profile.updateImageName(profileDto.getProfileImage().getOriginalFilename());
//        profile.updateImagePath(profileDto.getProfileImage().get);
        updateImage(multipartFile, profile, request);
        profile.updateRole(profileDto.getRole());
        profile.updateNickname(profileDto.getNickname());

        return profile;
    }

    @Transactional(readOnly = false)
    public Profile createMission(Long userPk) {

        Profile profile = profileRepository.findProfileByUserPk(userPk);

        if (profile.getMission_target() == null) {

            String[] missionList = {"전화하기", "어깨 주물러드리기", "소원들어주기"};
            Family family = familyRepository.findFamilyByUserPk(userPk);

            List<MainProfileResDto> familyProfiles = profileRepository.getProfileListByFamilyId(family.getId());
            int roleRandom = random.nextInt(familyProfiles.size());
            int missionRandom = random.nextInt(missionList.length);
            if (familyProfiles.get(roleRandom).getRole().equals(profile.getRole())) {
                roleRandom = (roleRandom + 1) % familyProfiles.size();
            }

            String missionTarget = familyProfiles.get(roleRandom).getRole();

            String missionContent = missionTarget + missionList[missionRandom];

            profile.updateMissionContent(missionContent);

            profile.updateMissionTarget(missionTarget);

        }
        return profile;
    }


    @Transactional(readOnly = false)
    public void updateStatus(Profile profile, StatusReqDto statusDto) {
        profile.updateEmotion(statusDto.getEmotion());
        profile.updateComment(statusDto.getComment());

        profileRepository.save(profile);
    }

    @Transactional(readOnly = false)
    public void updateImage(MultipartFile multipartFile, Profile profile, HttpServletRequest request) {
        try {
            String separ = File.separator;
            String today = new SimpleDateFormat("yyMMdd").format(new Date());

            File file = new File("");
//            String rootPath = file.getAbsolutePath().split("src")[0];

//            String savePath = "../"+"profileImg"+separ+today;
            String savePath = request.getServletContext().getRealPath("/resources/profileImage");
            log.info(savePath);
            if (!new File(savePath).exists()) {
                try {
                    new File(savePath).mkdirs();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            String originFileName = multipartFile.getOriginalFilename();
            String saveFileName = UUID.randomUUID().toString() + originFileName.substring(originFileName.lastIndexOf("."));

            String filePath = savePath + separ + saveFileName;
            multipartFile.transferTo(new File(filePath));
            profile.updateImagePath("/resources/profileImage/" + saveFileName);
            profile.updateImageName(originFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(readOnly = false)
    public String enrollImage(MultipartFile multipartFile, HttpServletRequest request) {
        try {
            String separ = File.separator;
//            String today= new SimpleDateFormat("yyMMdd").format(new Date());

            File file = new File("");
//            String rootPath = file.getAbsolutePath().split("src")[0];

//            String savePath = "../"+"profileImg"+separ+today;
            String savePath = request.getServletContext().getRealPath("/resources/profileImage");
            if (!new File(savePath).exists()) {
                try {
                    new File(savePath).mkdirs();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            String originFileName = multipartFile.getOriginalFilename();
            String saveFileName = UUID.randomUUID().toString() + originFileName.substring(originFileName.lastIndexOf("."));

            String filePath = savePath + separ + saveFileName;
            multipartFile.transferTo(new File(filePath));
            String realPath = "/resources/profileImage/" + saveFileName;
            return realPath + "#" + originFileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Transactional
    public String findImage(Long userPk) {
        Profile profile = profileRepository.findProfileByUserPk(userPk);
        return profile.getImagePath();
    }

    @Transactional
    public Profile findProfileByUserPk(Long userPk) {
        Profile profile = profileRepository.findProfileByUserPk(userPk);
        return profile;
    }


}
