package com.ssafy.api.service;

import com.ssafy.api.dto.res.FamilyCodeResDto;
import com.ssafy.api.entity.Family;
import com.ssafy.api.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

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
}
