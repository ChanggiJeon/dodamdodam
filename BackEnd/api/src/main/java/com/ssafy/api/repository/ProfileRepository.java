package com.ssafy.api.repository;

import com.ssafy.api.entity.Profile;
import com.ssafy.api.repository.querydsl.ProfileRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long>, ProfileRepoCustom {

}
