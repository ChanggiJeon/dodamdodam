package com.ssafy.family.data.repository

import com.ssafy.family.data.remote.api.StatusAPI
import com.ssafy.family.data.remote.res.FamilyPictureRes
import com.ssafy.family.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.Exception

class StatusRepositoryImpl(
    private val api: StatusAPI,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher
): StatusRepository {
    override suspend fun getFamilyPicture():
        Resource<FamilyPictureRes> =
        withContext(ioDispatcher) {
            try {
                val response = api.getFamilyPicture()
                when {
                    response.isSuccessful -> {
                        Resource.success(response.body()!!)
                    }
                    else -> {
                        Resource.error(null, "응답 에러")
                    }
                }
            } catch (e: Exception) {
                Resource.error(null, "통신 에러 $e")
            }
        }
}
