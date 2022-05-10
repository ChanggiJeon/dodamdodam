package com.ssafy.family.data.repository

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.res.FamilyPictureRes
import com.ssafy.family.data.remote.res.StatusRes
import com.ssafy.family.util.Resource

interface StatusRepository {
    suspend fun getFamilyPicture(): Resource<FamilyPictureRes>

    suspend fun getMyStatus(): Resource<StatusRes>

    suspend fun editMyStatus(emotion: String, comment: String): Resource<BaseResponse>
}