package com.ssafy.family.data.repository

import com.ssafy.family.config.ApplicationClass
import com.ssafy.family.data.req.LoginReq
import com.ssafy.family.data.api.AccountAPI
import com.ssafy.family.data.res.LoginRes
import com.ssafy.family.util.Resource
import java.lang.Exception

class AccountRepository {
    val accountAPI = ApplicationClass.sRetrofit.create(AccountAPI::class.java)

    suspend fun login(user: LoginReq): Resource<LoginRes> {
        return try{
            val response = accountAPI.login(user)
            if(response.isSuccessful){
                Resource.success(response.body()!!)
            }else{
                Resource.error(null,"오류")
            }
        }catch (e:Exception){
            Resource.error(null,"서버와 연결오류")
        }
    }

}