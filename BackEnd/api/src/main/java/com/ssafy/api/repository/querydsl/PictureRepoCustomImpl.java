package com.ssafy.api.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.api.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PictureRepoCustomImpl implements PictureRepoCustom{
    private final JPAQueryFactory jpaQueryFactory;
    QAlbum album = QAlbum.album;
    QFamily family = QFamily.family;
    QHashTag hashTag = QHashTag.hashTag;
    QPicture picture = QPicture.picture;
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

    @Override
    public Picture findMainPictureByAlbumId(long albumId) {
        return jpaQueryFactory.select(picture)
                .from(picture)
                .where(picture.album.id.eq(albumId).and(
                        picture.is_main.eq(true)
                ))
                .leftJoin(picture.album, album)
                .fetchJoin()
                .fetchOne();
    }
}
