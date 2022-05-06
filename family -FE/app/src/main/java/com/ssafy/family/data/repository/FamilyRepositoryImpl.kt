package com.ssafy.family.data.repository

import android.util.Log
import com.ssafy.family.data.remote.api.FamilyAPI
import com.ssafy.family.data.remote.req.CreateFamilyReq
import com.ssafy.family.data.remote.req.JoinFamilyReq
import com.ssafy.family.data.remote.res.FamilyRes
import com.ssafy.family.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.lang.Exception

class FamilyRepositoryImpl(
    private val api: FamilyAPI,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher
): FamilyRepository {
    val TAG: String = "로그"

    override suspend fun createFamily(profile: CreateFamilyReq, imageFile: File?): Resource<FamilyRes> = withContext(ioDispatcher) {
        try {
            val role = getBody("role", profile.role)
            val nickname = getBody("nickname", profile.nickname)
            val birthday = getBody("birthday", profile.birthday)
            val image = convertFileToMultipart(imageFile)
            val response = api.createFamily(role, nickname, birthday, image)
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
            Resource.error(null, "통신 에러 $e")
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

    private fun convertFileToMultipart(file: File?): MultipartBody.Part? {
        if (file == null) {
            return null
        } else {
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            return MultipartBody.Part.createFormData("image", file.name, requestFile)
        }
    }

    private fun getBody(name: String, value: Any): MultipartBody.Part {
        return MultipartBody.Part.createFormData(name, value.toString())
    }
}