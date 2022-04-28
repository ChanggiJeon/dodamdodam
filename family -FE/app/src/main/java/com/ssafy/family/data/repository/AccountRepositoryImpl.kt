package com.ssafy.family.data.repository

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.LoginReq
import com.ssafy.family.data.remote.api.AccountAPI
import com.ssafy.family.data.remote.req.AddFcmReq
import com.ssafy.family.data.remote.req.SignUpReq
import com.ssafy.family.data.remote.req.findIdReq
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

    override suspend fun addFcm(fcmToken: AddFcmReq): Resource<BaseResponse> {
        return try{
            val response = api.addFcm(fcmToken)
            if(response.isSuccessful){
                Resource.success(response.body()!!)
            }else{
                Resource.error(null,"오류")
            }
        }catch (e:Exception){
            Resource.error(null,"서버와 연결오류")
        }
    }

    override suspend fun signUp(signUpReq: SignUpReq): Resource<BaseResponse> {
        return try{
            val response = api.signUp(signUpReq)
            if(response.isSuccessful){
                Resource.success(response.body()!!)
            }else{
                Resource.error(null,"오류")
            }
        }catch (e:Exception){
            Resource.error(null,"서버와 연결오류")
        }
    }

    override suspend fun findId(findIdReq: findIdReq): Resource<BaseResponse> {
        return try{
            val response = api.findId(findIdReq)
            if(response.isSuccessful){
                Resource.success(response.body()!!)
            }else{
                Resource.error(null,"오류")
            }
        }catch (e:Exception){
            Resource.error(null,"서버와 연결오류")
        }
    }

    override suspend fun newPassword(newIdPwReq: LoginReq): Resource<BaseResponse> {
        return try{
            val response = api.newPassword(newIdPwReq)
            if(response.isSuccessful){
                Resource.success(response.body()!!)
            }else{
                Resource.error(null,"오류")
            }
        }catch (e:Exception){
            Resource.error(null,"서버와 연결오류")
        }
    }

    override suspend fun idCheck(userId: String): Resource<BaseResponse> {
        return try{
            val response = api.idCheck(userId)
            if(response.isSuccessful){
                Resource.success(response.body()!!)
            }else{
                Resource.error(null,"오류")
            }
        }catch (e:Exception){
            Resource.error(null,"서버와 연결오류")
        }
    }

    override suspend fun updateBirthDay(birthday: String): Resource<BaseResponse> {
        return try{
            val response = api.updateBirthDay(birthday)
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