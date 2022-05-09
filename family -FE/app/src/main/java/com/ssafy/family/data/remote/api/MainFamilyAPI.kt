package com.ssafy.family.data.remote.api

import com.ssafy.family.data.remote.res.MissionRes
import retrofit2.Response
import retrofit2.http.GET

interface MainFamilyAPI {
    @GET("/api/main/mission")
    suspend fun getTodayMission():Response<MissionRes>
}