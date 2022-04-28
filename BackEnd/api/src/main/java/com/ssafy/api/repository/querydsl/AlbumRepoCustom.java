package com.ssafy.api.repository.querydsl;

import com.ssafy.api.entity.Album;
import com.ssafy.api.entity.HashTag;
import com.ssafy.api.entity.Picture;

import java.util.List;

public interface AlbumRepoCustom {

    List<Album> findAlbumByFamilyId(long familyId);
    Album findAlbumByAlbumId(long albumId);


}
