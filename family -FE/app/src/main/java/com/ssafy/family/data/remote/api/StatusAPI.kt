package com.ssafy.family.data.remote.api

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.EditStatusReq
import com.ssafy.family.data.remote.res.FamilyPictureRes
import com.ssafy.family.data.remote.res.StatusRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface StatusAPI {
    @GET("api/family/picture")
    suspend fun getFamilyPicture(): Response<FamilyPictureRes>
    @GET("api/profile/comment")
    suspend fun getMyStatus(): Response<StatusRes>
    @PATCH("api/profile/status")
    suspend fun editMyStatus(@Body editStatusReq: EditStatusReq): Response<BaseResponse>
}