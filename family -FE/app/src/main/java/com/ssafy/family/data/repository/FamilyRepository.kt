package com.ssafy.family.data.repository

import com.ssafy.family.data.remote.req.CreateFamilyReq
import com.ssafy.family.data.remote.req.JoinFamilyReq
import com.ssafy.family.data.remote.res.FamilyRes
import com.ssafy.family.util.Resource
import java.io.File

interface FamilyRepository {
    suspend fun createFamily(profile: CreateFamilyReq, imageFile: File?): Resource<FamilyRes>

    suspend fun joinFamily(profile: JoinFamilyReq): Resource<FamilyRes>

    suspend fun checkFamilyCode(familyCode: String): Resource<FamilyRes>
}