package com.ssafy.api.repository.querydsl;

import com.ssafy.api.entity.HashTag;

import java.util.List;

public interface HashTagRepoCustom {
    List<HashTag> findHashTagsByAlbumId(long albumId);
}
