package com.ssafy.family.data.api

import com.ssafy.family.data.req.LoginReq
import com.ssafy.family.data.UserInfo
import com.ssafy.family.data.res.LoginRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AccountAPI {
    @POST("/api/user/signin")
    suspend fun login(@Body user : LoginReq):Response<LoginRes>
}