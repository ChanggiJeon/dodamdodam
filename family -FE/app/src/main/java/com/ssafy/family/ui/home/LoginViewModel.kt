package com.ssafy.family.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.AddFcmReq
import com.ssafy.family.data.remote.req.LoginReq
import com.ssafy.family.data.remote.req.SignUpReq
import com.ssafy.family.data.remote.req.findIdReq
import com.ssafy.family.data.remote.res.LoginRes
import com.ssafy.family.data.repository.AccountRepository
import com.ssafy.family.util.LoginUtil
import com.ssafy.family.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val accountRepository: AccountRepository) :
    ViewModel() {

    //    val accountRepository = AccountRepositoryImpl()
    var isAutoLogin = LoginUtil.isAutoLogin()

    private val _loginRequestLiveData = MutableLiveData<Resource<LoginRes>>()
    val loginRequestLiveData: LiveData<Resource<LoginRes>>
        get() = _loginRequestLiveData

    private val _baseResLiveData = MutableLiveData<Resource<BaseResponse>>()
    val baseResponse: LiveData<Resource<BaseResponse>>
        get() = _baseResLiveData

    private val _findLiveData = MutableLiveData<Resource<BaseResponse>>()
    val findLiveData: LiveData<Resource<BaseResponse>>
        get() = _findLiveData

    private val _idCheckLiveData = MutableLiveData<Resource<BaseResponse>>()
    val idCheckLiveData: LiveData<Resource<BaseResponse>>
        get() = _idCheckLiveData

    private val _signUpLiveData = MutableLiveData<Resource<BaseResponse>>()
    val signUpLiveData: LiveData<Resource<BaseResponse>>
        get() = _signUpLiveData

    fun Login(user: LoginReq) = viewModelScope.launch {
        _loginRequestLiveData.postValue(Resource.loading(null))
        withContext(Dispatchers.IO) {
            _loginRequestLiveData.postValue(accountRepository.login(user))
        }
    }

    fun addFCM(fcmToken: AddFcmReq) = viewModelScope.launch {
        _baseResLiveData.postValue(Resource.loading(null))
        withContext(Dispatchers.IO) {
            _baseResLiveData.postValue(accountRepository.addFcm(fcmToken))
        }
    }

    fun findId(findIdReq: findIdReq) = viewModelScope.launch {
        _findLiveData.postValue(Resource.loading(null))
        withContext(Dispatchers.IO) {
            _findLiveData.postValue(accountRepository.findId(findIdReq))
        }
    }
    fun newPassword(newIdPwReq:LoginReq) = viewModelScope.launch {
        _baseResLiveData.postValue(Resource.loading(null))
        withContext(Dispatchers.IO) {
            _baseResLiveData.postValue(accountRepository.newPassword(newIdPwReq))
        }
    }
    fun idCheck(userId:String)=viewModelScope.launch {
        _idCheckLiveData.postValue(Resource.loading(null))
        withContext(Dispatchers.IO) {
            _idCheckLiveData.postValue(accountRepository.idCheck(userId))
        }
    }
    fun signUp(signUpReq: SignUpReq)=viewModelScope.launch {
        _signUpLiveData.postValue(Resource.loading(null))
        withContext(Dispatchers.IO) {
            _signUpLiveData.postValue(accountRepository.signUp(signUpReq))
        }
    }
}