package com.ssafy.family.data.repository

import com.ssafy.family.R
import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.api.AccountAPI
import com.ssafy.family.data.remote.api.CalendarAPI
import com.ssafy.family.data.remote.req.*
import com.ssafy.family.data.remote.res.LoginRes
import com.ssafy.family.data.remote.res.ScheduleRes
import com.ssafy.family.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class CalendarRepositoryImpl(
    private val api: CalendarAPI,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher
) : CalendarRepository {

    override suspend fun getDaySchedule(day:String): Resource<List<ScheduleRes>>  = withContext(ioDispatcher){
        try {
            val response = api.getDaySchedule(day)
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                response.code() == 403 -> {
                    Resource.expired(response.body()!!)
                }
                else -> {
                    Resource.error(null, "오류")
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버와 연결오류")
        }
    }

    override suspend fun getMonthSchedule(month:String): Resource<List<ScheduleRes>> = withContext(ioDispatcher) {
        try {
            val response = api.getMonthSchedule(month)
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                response.code() == 403 ->{
                    Resource.expired(response.body()!!)
                }
                else -> {
                    Resource.error(null, "오류")
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버와 연결오류")
        }
    }

    override suspend fun getSchedule(scheduleId:Int): Resource<ScheduleRes>  = withContext(ioDispatcher){
        try {
            val response = api.getSchedule(scheduleId)
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                response.code() == 403 -> {
                    Resource.expired(response.body()!!)
                }
                else -> {
                    Resource.error(null, "오류")
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버와 연결오류")
        }
    }

    override suspend fun addSchedule(scheduleReq: ScheduleReq): Resource<BaseResponse> = withContext(ioDispatcher) {
        try {
            val response = api.addSchedule(scheduleReq)
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                response.code() == 403 ->{
                    Resource.expired(response.body()!!)
                }
                else -> {
                    Resource.error(null, "오류")
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버와 연결오류")
        }
    }

}