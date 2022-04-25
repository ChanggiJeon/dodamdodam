package com.ssafy.api.repository.querydsl;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.api.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlbumRepoCustomImpl implements AlbumRepoCustom{
    private final JPAQueryFactory jpaQueryFactory;

    QAlbum album = QAlbum.album;
    QFamily family = QFamily.family;
    QHashTag hashTag = QHashTag.hashTag;
    QPicture picture = QPicture.picture;
    //앨범 찾기
    //앨범아이디로 해시태그, 사진, 리액션 찾기
    @Override
    public List<Album> findAlbumByFamilyId(long familyId) {

        return jpaQueryFactory.select(album)
                .from(album)
                .where(album.family.id.eq(familyId))
                .leftJoin(album.family, family)
                .orderBy(album.date.desc())
                .fetch();
    }

    @Override
    public Album findAlbumByAlbumId(long albumId) {

        return jpaQueryFactory.select(album)
                .from(album)
                .where(album.id.eq(albumId))
                .fetchOne();
    }

    @Override
    public List<String> findHashTagsByAlbumId(long albumId) {
        return jpaQueryFactory.select(hashTag.text)
                .from(hashTag)
                .where(hashTag.album.id.eq(albumId))
                .leftJoin(hashTag.album, album)
                .fetch();
    }


    @Override
    public List<Picture> findPicturesByAlbumId(long albumId) {
        return jpaQueryFactory.select(picture)
                .from(picture)
                .where(picture.album.id.eq(albumId))
                .leftJoin(picture.album, album)
                .fetch();
    }

    @Override
    public Picture findPictureByPictureId(long pictureId) {
        return jpaQueryFactory.select(picture)
                .from(picture)
                .where(picture.id.eq(pictureId))
                .fetchOne();
    }


}
