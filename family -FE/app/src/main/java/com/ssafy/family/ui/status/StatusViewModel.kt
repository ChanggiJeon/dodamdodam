package com.ssafy.family.ui.status

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.res.FamilyPictureRes
import com.ssafy.family.data.remote.res.StatusRes
import com.ssafy.family.data.repository.StatusRepository
import com.ssafy.family.util.Constants.TAG
import com.ssafy.family.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatusViewModel @Inject constructor(private val statusRepository: StatusRepository) :
    ViewModel() {
    // 가족사진
    private val _familyPicture = MutableLiveData<Resource<FamilyPictureRes>>()
    val familyPicture: LiveData<Resource<FamilyPictureRes>>
        get() = _familyPicture
    // 오늘의 한마디
    private val _todaysMessage = MutableLiveData<Resource<StatusRes>>()
    val todaysMessage: LiveData<Resource<StatusRes>>
        get() = _todaysMessage
    // 수정 요청 res
    private val _editStatusResponse = MutableLiveData<Resource<BaseResponse>>()
    val editStatusResponse: LiveData<Resource<BaseResponse>>
        get() = _editStatusResponse

    fun getFamilyPicture() = viewModelScope.launch {
        _familyPicture.postValue(Resource.loading(null))
        _familyPicture.postValue(statusRepository.getFamilyPicture())
        Log.d(TAG, "StatusViewModel - getFamilyPicture() called ${_familyPicture.value?.data?.dataset}")
    }

    fun getMyStatus() = viewModelScope.launch {
        _todaysMessage.postValue(Resource.loading(null))
        val res = statusRepository.getMyStatus()
        _todaysMessage.postValue(res)
        Log.d(TAG, "StatusViewModel - getMyStatus() $res")
    }

    fun editMyStatus(emotion: String, comment: String) = viewModelScope.launch {
        _editStatusResponse.postValue(Resource.loading(null))
        _editStatusResponse.postValue(statusRepository.editMyStatus(emotion = emotion, comment = comment))
    }
}