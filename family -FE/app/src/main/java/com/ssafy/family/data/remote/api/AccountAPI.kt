package com.ssafy.family.data.remote.api

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.AddFcmReq
import com.ssafy.family.data.remote.req.LoginReq
import com.ssafy.family.data.remote.req.SignUpReq
import com.ssafy.family.data.remote.req.findIdReq
import com.ssafy.family.data.remote.res.LoginRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AccountAPI {
    @POST("/api/user/signin")
    suspend fun login(@Body user : LoginReq):Response<LoginRes>

    @POST("/api/user/fcm")
    suspend fun addFcm(@Body fcmToken:AddFcmReq):Response<BaseResponse>

    @POST("/api/user/signup")
    suspend fun signUp(@Body signUpReq: SignUpReq):Response<BaseResponse>

    @POST("/api/user/findId")
    suspend fun findId(@Body findIdReq: findIdReq):Response<BaseResponse>

    @POST("/api/user/newpassword")
    suspend fun newPassword(@Body newIdPwReq:LoginReq):Response<BaseResponse>

    @GET("/api/user")
    suspend fun idCheck(@Query("userId") userId:String):Response<BaseResponse>

    @GET("api/user/birthday")
    suspend fun updateBirthDay(@Query("birthday")birthday:String):Response<BaseResponse>
}