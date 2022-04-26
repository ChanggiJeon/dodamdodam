package com.ssafy.api.repository;


import com.ssafy.api.entity.Picture;
import com.ssafy.api.repository.querydsl.PictureRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<Picture, Long>, PictureRepoCustom {
}
