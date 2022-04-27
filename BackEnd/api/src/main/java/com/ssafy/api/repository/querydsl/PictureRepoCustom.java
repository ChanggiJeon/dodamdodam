package com.ssafy.api.repository.querydsl;

import com.ssafy.api.entity.Picture;

import java.util.List;

public interface PictureRepoCustom {

    List<Picture> findPicturesByAlbumId(long albumId);
    Picture findPictureByPictureId(long pictureId);
    Picture findMainPictureByAlbumId(long albumId);
}
