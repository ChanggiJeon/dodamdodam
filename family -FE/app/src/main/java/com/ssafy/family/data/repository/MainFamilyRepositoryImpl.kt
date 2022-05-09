package com.ssafy.family.data.repository

import com.ssafy.family.data.remote.api.MainFamilyAPI
import com.ssafy.family.data.remote.res.MissionRes
import com.ssafy.family.util.Resource
import kotlinx.coroutines.CoroutineDispatcher

class MainFamilyRepositoryImpl(
    private val api: MainFamilyAPI,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher
) :MainFamilyRepository{
    override suspend fun getTodayMission(): Resource<MissionRes> {
        TODO("Not yet implemented")
    }
}