package com.ssafy.family.data.repository

import android.util.Log
import com.ssafy.family.data.remote.api.FamilyAPI
import com.ssafy.family.data.remote.req.CreateFamilyReq
import com.ssafy.family.data.remote.req.JoinFamilyReq
import com.ssafy.family.data.remote.res.FamilyRes
import com.ssafy.family.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.Exception

class FamilyRepositoryImpl(
    private val api: FamilyAPI,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher
): FamilyRepository {
    val TAG: String = "로그"

    override suspend fun createFamily(profile: CreateFamilyReq): Resource<FamilyRes> = withContext(ioDispatcher) {
        try {
            val response = api.createFamily(profile)
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                else -> {
                    Log.d(TAG, "FamilyRepositoryImpl - createFamily() ErrorCode:${response.code().toString()}")
                    Resource.error(null, "응답 에러")
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버 에러")
        }
    }

    override suspend fun joinFamily(profile: JoinFamilyReq): Resource<FamilyRes> = withContext(ioDispatcher) {
        try {
            val response = api.joinFamily(profile)
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                else -> {
                    Resource.error(null, "응답 에러")
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버 에러")
        }
    }

}