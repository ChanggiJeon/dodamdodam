package com.ssafy.family.data.remote.api

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.LoginReq
import com.ssafy.family.data.remote.req.ScheduleReq
import com.ssafy.family.data.remote.res.ScheduleRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CalendarAPI {

    @GET("/api/schedule/day/{day}")
    suspend fun getDaySchedule(@Query("day") day:String): Response<List<ScheduleRes>>

    @GET("/api/schedule/month/{month}")
    suspend fun getMonthSchedule(@Query("month") month:String): Response<List<ScheduleRes>>

    @GET("/api/schedule/{scheduleId}")
    suspend fun getSchedule(@Query("scheduleId") scheduleId:Int): Response<ScheduleRes>

    @POST("api/schedule/create")
    suspend fun addSchedule(@Body scheduleReq: ScheduleReq): Response<BaseResponse>

}