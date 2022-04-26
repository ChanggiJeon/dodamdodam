package com.ssafy.family.data.remote.api

import com.ssafy.family.data.remote.req.LoginReq
import com.ssafy.family.data.remote.res.LoginRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AccountAPI {
    @POST("/api/user/signin")
    suspend fun login(@Body user : LoginReq):Response<LoginRes>
}