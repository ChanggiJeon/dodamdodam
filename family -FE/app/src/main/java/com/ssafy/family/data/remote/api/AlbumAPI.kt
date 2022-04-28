package com.ssafy.family.data.remote.api

import com.ssafy.family.data.remote.res.AlbumRes
import retrofit2.Response
import retrofit2.http.GET

interface AlbumAPI {

    @GET("/api/album")
    suspend fun allAlbum():Response<AlbumRes>
}