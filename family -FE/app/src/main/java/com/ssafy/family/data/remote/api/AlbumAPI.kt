package com.ssafy.family.data.remote.api

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.res.AlbumDetailRes
import com.ssafy.family.data.remote.res.AlbumRes
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Query

interface AlbumAPI {

    @GET("/api/album")
    suspend fun allAlbum():Response<AlbumRes>
    @GET("/api/album/{albumId}")
    suspend fun detailAlbum(@Query("albumId")albumId:Int):Response<AlbumDetailRes>
    @DELETE("/api/album/{reactionId}}")
    suspend fun deleteReaction(@Query("reactionId")reactionId: Int):Response<BaseResponse>
}