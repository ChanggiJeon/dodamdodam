package com.ssafy.api.service;



import com.ssafy.api.dto.req.ProfileReqDto;
import com.ssafy.api.dto.req.StatusReqDto;
import com.ssafy.api.entity.Profile;
import com.ssafy.api.entity.User;
import com.ssafy.api.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserService userService;
    @Transactional(readOnly = false)
    public void enrollProfile(Profile profile){
        Profile result = profileRepository.save(profile);
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
    public Profile updateProfile(String userId, ProfileReqDto profileDto, MultipartFile multipartFile, HttpServletRequest request){
        Profile profile = profileRepository.findProfileById(userId);
        //이미지 originalName, path 수정
//        profile.updateImageName(profileDto.getProfileImage().getOriginalFilename());
//        profile.updateImagePath(profileDto.getProfileImage().get);
        updateImage(multipartFile, profile, request);
        profile.updateRole(profileDto.getRole());
        profile.updateNickname(profileDto.getNickname());

        // userService에서 birthday 업데이트?
        return profile;
    }

    @Transactional(readOnly = false)
    public Profile enrollMission(String userId, String missionContent){
        Profile profile = profileRepository.findProfileById(userId);
        profile.updateMissionContent(missionContent);
        return profile;
    }

    @Transactional(readOnly = false)
    public Profile updateStatus(String userId, StatusReqDto statusDto){
        Profile profile = profileRepository.findProfileById(userId);
        profile.updateEmotion(statusDto.getEmotion());
        profile.updateComment(statusDto.getComment());
        return profile;
    }

    @Transactional(readOnly = false)
    public void updateImage(MultipartFile multipartFile, Profile profile, HttpServletRequest request){
        try{
            String separ = File.separator;
            String today= new SimpleDateFormat("yyMMdd").format(new Date());

            File file = new File("");
//            String rootPath = file.getAbsolutePath().split("src")[0];

//            String savePath = "../"+"profileImg"+separ+today;
            String savePath = request.getServletContext().getRealPath("/profileImage");
            log.info(savePath);
            if(!new File(savePath).exists()){
                try{
                    new File(savePath).mkdirs();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            String originFileName = multipartFile.getOriginalFilename();
            String saveFileName = UUID.randomUUID().toString() + originFileName.substring(originFileName.lastIndexOf("."));

            String filePath = savePath+separ+saveFileName;
            multipartFile.transferTo(new File(filePath));
            profile.updateImagePath("/profileImage/"+saveFileName);
            profile.updateImageName(originFileName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Transactional(readOnly = false)
    public String enrollImage(MultipartFile multipartFile,  HttpServletRequest request){
        try{
            String separ = File.separator;
//            String today= new SimpleDateFormat("yyMMdd").format(new Date());

            File file = new File("");
//            String rootPath = file.getAbsolutePath().split("src")[0];

//            String savePath = "../"+"profileImg"+separ+today;
            String savePath = request.getServletContext().getRealPath("/profileImage");
            if(!new File(savePath).exists()){
                try{
                    new File(savePath).mkdirs();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            String originFileName = multipartFile.getOriginalFilename();
            String saveFileName = UUID.randomUUID().toString() + originFileName.substring(originFileName.lastIndexOf("."));

            String filePath = savePath+separ+saveFileName;
            multipartFile.transferTo(new File(filePath));
            String realPath = "/profileImage/"+saveFileName;
            return realPath+"#"+originFileName;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    @Transactional
    public String findImage(String userId){
        Profile profile = profileRepository.findProfileById(userId);
        return profile.getImagePath();
    }




}
