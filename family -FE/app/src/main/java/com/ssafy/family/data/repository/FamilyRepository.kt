package com.ssafy.family.data.repository

import com.ssafy.family.data.remote.req.CreateFamilyReq
import com.ssafy.family.data.remote.req.JoinFamilyReq
import com.ssafy.family.data.remote.res.FamilyRes
import com.ssafy.family.util.Resource

interface FamilyRepository {
    suspend fun createFamily(profile: CreateFamilyReq): Resource<FamilyRes>

    suspend fun joinFamily(profile: JoinFamilyReq): Resource<FamilyRes>
}