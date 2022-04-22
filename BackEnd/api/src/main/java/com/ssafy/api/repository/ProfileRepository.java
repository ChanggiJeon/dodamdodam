package com.ssafy.api.repository;

import com.ssafy.api.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long>, ProfileRepoCommon{
}
