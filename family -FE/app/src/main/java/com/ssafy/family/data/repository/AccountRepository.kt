package com.ssafy.family.data.repository

import com.ssafy.family.data.remote.req.LoginReq
import com.ssafy.family.data.remote.res.LoginRes
import com.ssafy.family.util.Resource

interface AccountRepository {
    suspend fun login(user: LoginReq): Resource<LoginRes>
    //회원가입
}