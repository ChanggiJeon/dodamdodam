package com.ssafy.family.data.repository

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.res.FamilyPictureRes
import com.ssafy.family.util.Resource

interface StatusRepository {
    suspend fun getFamilyPicture(): Resource<FamilyPictureRes>

//    suspend fun editStatus(emotion: String, comment: String): Resource<BaseResponse>
}