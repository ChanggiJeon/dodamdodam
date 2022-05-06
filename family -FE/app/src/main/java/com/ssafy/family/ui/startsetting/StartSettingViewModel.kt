package com.ssafy.family.ui.startsetting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.family.data.remote.req.CreateFamilyReq
import com.ssafy.family.data.remote.req.JoinFamilyReq
import com.ssafy.family.data.remote.res.FamilyRes
import com.ssafy.family.data.repository.FamilyRepository
import com.ssafy.family.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class StartSettingViewModel @Inject constructor(private val familyRepository: FamilyRepository) :
    ViewModel() {

    private val _familyRequestLiveData = MutableLiveData<Resource<FamilyRes>>()
    val familyRequestLiveData: LiveData<Resource<FamilyRes>>
        get() = _familyRequestLiveData

    fun createFamily(profile: CreateFamilyReq, imageFile : File?) = viewModelScope.launch {
        _familyRequestLiveData.postValue(Resource.loading(null))
        _familyRequestLiveData.postValue(familyRepository.createFamily(profile, imageFile))
    }

    fun joinFamily(profile: JoinFamilyReq) = viewModelScope.launch {
        _familyRequestLiveData.postValue(Resource.loading(null))
        _familyRequestLiveData.postValue(familyRepository.joinFamily(profile))
    }
}