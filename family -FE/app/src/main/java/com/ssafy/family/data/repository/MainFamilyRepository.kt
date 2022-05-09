package com.ssafy.family.data.repository

import com.ssafy.family.data.remote.res.MissionRes
import com.ssafy.family.util.Resource

interface MainFamilyRepository {
    suspend fun getTodayMission():Resource<MissionRes>
}