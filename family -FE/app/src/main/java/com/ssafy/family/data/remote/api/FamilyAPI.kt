package com.ssafy.family.data.remote.api

import com.ssafy.family.data.remote.req.CreateFamilyReq
import com.ssafy.family.data.remote.req.JoinFamilyReq
import com.ssafy.family.data.remote.res.FamilyRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FamilyAPI {
    @POST("/api/family/create")
    suspend fun createFamily(@Body createFamilyReq: CreateFamilyReq): Response<FamilyRes>

    @POST("/api/family/join")
    suspend fun joinFamily(@Body joinFamilyReq: JoinFamilyReq): Response<FamilyRes>
}