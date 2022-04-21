package com.ssafy.api.service;

import com.ssafy.api.dto.req.FamilyPictureReqDto;
import com.ssafy.api.entity.Family;
import com.ssafy.api.exception.CustomException;
import com.ssafy.api.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import static com.ssafy.api.exception.CustomErrorCode.INVALID_REQUEST;

@Service
@Slf4j
@RequiredArgsConstructor
public class FamilyService {
    private final FamilyRepository familyRepository;

    public void createFamily() {
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
        Family family = new Family();
        family.setCode(key);
        familyRepository.save(family);
    }

    public Family getFamilyCode(long familyId) {

        return familyRepository.findById(familyId);
    }


    public void updateFamilyPicture(MultipartFile picture, String path) {
        String imgFileName = picture.getOriginalFilename();
        File dir = new File(path);
        Family family = familyRepository.findById(2L);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        try{
            if(family.getPicture() != null) {
                File file = new File(path + "/" + family.getPicture());
                file.delete();
            }
            File file = new File(path + "/" + picture.getOriginalFilename());
            picture.transferTo(file);
        } catch (Exception e){
            throw new CustomException(INVALID_REQUEST, "파일이 없습니다.");
        }
        family.setPicture(imgFileName);
        familyRepository.save(family);
    }
}
