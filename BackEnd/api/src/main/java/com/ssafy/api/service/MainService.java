package com.ssafy.api.service;

import com.ssafy.api.dto.res.MainProfileResDto;
import com.ssafy.api.repository.FamilyRepository;
import com.ssafy.api.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainService {

    private final ProfileRepository profileRepository;
    private final FamilyRepository familyRepository;

    public List<MainProfileResDto> getProfileListByFamilyId(Long familyId, Long userPK) {
        return profileRepository.getProfileListByFamilyId(familyId).stream()
                .filter(profile -> !profile.getUserPk().equals(userPK))
                .collect(Collectors.toList());
    }
}
