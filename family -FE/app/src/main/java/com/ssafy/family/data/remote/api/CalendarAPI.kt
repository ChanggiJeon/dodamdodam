package com.ssafy.family.data.remote.api

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.ScheduleReq
import com.ssafy.family.data.remote.res.ScheduleRes
import retrofit2.Response
import retrofit2.http.*

interface CalendarAPI {

    @GET("/api/schedule/day/{day}")
    suspend fun getDaySchedule(@Path("day") day:String): Response<ScheduleRes>

    @GET("/api/schedule/month/{month}")
    suspend fun getMonthSchedule(@Path("month") month:String): Response<ScheduleRes>

    // TODO:
    @GET("/api/schedule/{scheduleId}")
    suspend fun getSchedule(@Path("scheduleId") scheduleId:Int): Response<ScheduleRes>

    @POST("api/schedule/create")
    suspend fun addSchedule(@Body scheduleReq: ScheduleReq): Response<BaseResponse>

}