package com.ssafy.family.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.family.data.repository.AccountRepository
import com.ssafy.family.data.remote.req.LoginReq
import com.ssafy.family.data.remote.res.LoginRes
import com.ssafy.family.util.LoginUtil
import com.ssafy.family.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(): ViewModel() {

    @Inject private lateinit var accountRepository: AccountRepository

//    val accountRepository = AccountRepositoryImpl()
    var isAutoLogin = LoginUtil.isAutoLogin()

    private val _loginRequestLiveData = MutableLiveData<Resource<LoginRes>>()
    val loginRequestLiveData : LiveData<Resource<LoginRes>>
        get() = _loginRequestLiveData

    fun Login(user:LoginReq) = viewModelScope.launch {
        _loginRequestLiveData.postValue(Resource.loading(null))
        withContext(Dispatchers.IO){
            _loginRequestLiveData.postValue(accountRepository.login(user))
        }
    }
}