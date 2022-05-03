package com.ssafy.family.data.repository


import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.AlbumReactionReq
import com.ssafy.family.data.remote.res.AlbumDetailRes
import com.ssafy.family.data.remote.res.AlbumRes
import com.ssafy.family.util.Resource

interface AlbumRepository {
    suspend fun findAllAlbum(): Resource<AlbumRes>
    suspend fun detailAlbum(albumId:Int):Resource<AlbumDetailRes>
    suspend fun deleteReaction(reactionId: Int):Resource<BaseResponse>
    suspend fun addReaction(albumReactionReq: AlbumReactionReq):Resource<BaseResponse>
}