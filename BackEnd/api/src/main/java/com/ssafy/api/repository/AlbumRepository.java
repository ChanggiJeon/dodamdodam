package com.ssafy.api.repository;

import com.ssafy.api.entity.Album;
import com.ssafy.api.entity.User;
import com.ssafy.api.repository.querydsl.AlbumRepoCustom;
import com.ssafy.api.repository.querydsl.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository  extends JpaRepository<Album, Long>, AlbumRepoCustom {
}
