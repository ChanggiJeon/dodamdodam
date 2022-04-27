package com.ssafy.family.data.repository

import com.ssafy.family.data.remote.req.LoginReq
import com.ssafy.family.data.remote.api.AccountAPI
import com.ssafy.family.data.remote.res.LoginRes
import com.ssafy.family.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import java.lang.Exception

class AccountRepositoryImpl (
    private val api: AccountAPI,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher
) : AccountRepository{
//    val accountAPI = ApplicationClass.sRetrofit.create(AccountAPI::class.java)

    override suspend fun login(user: LoginReq): Resource<LoginRes> {
        return try{
            val response = api.login(user)
            if(response.isSuccessful){
                Resource.success(response.body()!!)
            }else{
                Resource.error(null,"오류")
            }
        }catch (e:Exception){
            Resource.error(null,"서버와 연결오류")
        }
    }
    //회원가입

}