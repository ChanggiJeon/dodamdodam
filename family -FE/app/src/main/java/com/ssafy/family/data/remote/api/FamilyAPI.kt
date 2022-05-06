package com.ssafy.family.data.remote.api

import com.ssafy.family.data.remote.req.JoinFamilyReq
import com.ssafy.family.data.remote.res.FamilyRes
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FamilyAPI {
    @Multipart
    @POST("/api/family/create")
    suspend fun createFamily(
        @Part("role") role: MultipartBody.Part,
        @Part("nickname") nickname: MultipartBody.Part,
        @Part("birthday") birthday: MultipartBody.Part,
        @Part("image") image: MultipartBody.Part?
    ): Response<FamilyRes>

    @Multipart
    @POST("/api/family/join")
    suspend fun joinFamily(@Part joinFamilyReq: JoinFamilyReq): Response<FamilyRes>
}