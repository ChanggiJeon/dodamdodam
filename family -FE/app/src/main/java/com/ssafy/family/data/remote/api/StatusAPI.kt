package com.ssafy.family.data.remote.api

import com.ssafy.family.data.remote.res.FamilyPictureRes
import retrofit2.Response
import retrofit2.http.GET

interface StatusAPI {
    @GET("api/family/picture")
    suspend fun getFamilyPicture(): Response<FamilyPictureRes>
}