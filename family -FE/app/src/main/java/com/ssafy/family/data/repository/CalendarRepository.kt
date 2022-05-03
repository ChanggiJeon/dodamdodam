package com.ssafy.family.data.repository

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.*
import com.ssafy.family.data.remote.res.ScheduleRes
import com.ssafy.family.util.Resource

interface CalendarRepository {

    suspend fun getDaySchedule(day:String): Resource<List<ScheduleRes>>

    suspend fun getMonthSchedule(month:String): Resource<List<ScheduleRes>>

    suspend fun getSchedule(scheduleId:Int): Resource<ScheduleRes>

    suspend fun addSchedule(scheduleReq: ScheduleReq): Resource<BaseResponse>

}