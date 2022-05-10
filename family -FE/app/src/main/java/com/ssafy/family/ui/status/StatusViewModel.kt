package com.ssafy.family.ui.status

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.family.data.remote.res.FamilyPictureRes
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
    private val _todaysMessage = MutableLiveData<String>()
    val todaysMessage: LiveData<String>
        get() = _todaysMessage

    fun getFamilyPicture() = viewModelScope.launch {
        _familyPicture.postValue(Resource.loading(null))
        _familyPicture.postValue(statusRepository.getFamilyPicture())
        Log.d(TAG, "StatusViewModel - getFamilyPicture() called ${_familyPicture.value?.data?.dataset}")
    }
}