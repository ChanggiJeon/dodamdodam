package com.ssafy.api.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.api.entity.HashTag;
import com.ssafy.api.entity.QAlbum;
import com.ssafy.api.entity.QHashTag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HahTagRepoCustomImpl implements HashTagRepoCustom{
    private final JPAQueryFactory jpaQueryFactory;
    QHashTag hashTag = QHashTag.hashTag;
    QAlbum album = QAlbum.album;

    @Override
    public List<HashTag> findHashTagsByAlbumId(long albumId) {
        return jpaQueryFactory.select(hashTag)
                .from(hashTag)
                .where(hashTag.album.id.eq(albumId))
                .leftJoin(hashTag.album, album)
                .fetch();
    }

}
