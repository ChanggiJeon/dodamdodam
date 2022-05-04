package com.ssafy.family.data.remote.api

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.AlbumReactionReq
import com.ssafy.family.data.remote.res.AlbumDetailRes
import com.ssafy.family.data.remote.res.AlbumRes
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface AlbumAPI {

    @GET("/api/album")
    suspend fun allAlbum(): Response<AlbumRes>

    @GET("/api/album/{albumId}")
    suspend fun detailAlbum(@Path("albumId") albumId: Int): Response<AlbumDetailRes>

    @DELETE("/api/album/{reactionId}}")
    suspend fun deleteReaction(@Path("reactionId") reactionId: Int): Response<BaseResponse>

    @POST("/api/album/reaction")
    suspend fun addReaction(@Body albumReactionReq: AlbumReactionReq): Response<BaseResponse>

    @GET("/api/album/search/{keyword}")
    suspend fun searchAlbum(@Path("keyword") keyword: String): Response<AlbumRes>

    @Multipart
    @POST("api/album/create")
    suspend fun addAlbum(
        @Part("hashTags") hashTags: ArrayList<MultipartBody.Part>,
        @Part("date") date:MultipartBody.Part,
        @Part("mainIndex") mainIndex:MultipartBody.Part,
        @Part("multipartFiles") multipartFiles: ArrayList<MultipartBody.Part>
    ): Response<BaseResponse>
}