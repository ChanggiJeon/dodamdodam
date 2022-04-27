package com.ssafy.api.repository;

import com.ssafy.api.entity.AlbumReaction;
import com.ssafy.api.repository.querydsl.AlbumReactionRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumReactionRepository extends JpaRepository<AlbumReaction, Long>, AlbumReactionRepoCustom {
}
