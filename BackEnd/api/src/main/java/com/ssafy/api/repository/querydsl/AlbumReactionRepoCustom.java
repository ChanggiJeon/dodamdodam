package com.ssafy.api.repository.querydsl;

import com.ssafy.api.entity.AlbumReaction;

import java.util.List;

public interface AlbumReactionRepoCustom {
    List<AlbumReaction> findReactionsByAlbumId(long albumId);
    AlbumReaction findReactionByAlbumId(long albumId, long profileId);

}
