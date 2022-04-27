package com.ssafy.api.repository;

import com.ssafy.api.entity.HashTag;
import com.ssafy.api.repository.querydsl.HashTagRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashTagRepository extends JpaRepository<HashTag, Long>, HashTagRepoCustom {
}
