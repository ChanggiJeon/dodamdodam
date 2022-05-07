package com.ssafy.family.ui.startsetting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.family.data.remote.req.CreateFamilyReq
import com.ssafy.family.data.remote.req.JoinFamilyReq
import com.ssafy.family.data.remote.res.FamilyRes
import com.ssafy.family.data.repository.FamilyRepository
import com.ssafy.family.util.Constants.TAG
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
    private var _familyId = MutableLiveData<String>()

    val familyRequestLiveData: LiveData<Resource<FamilyRes>>
        get() = _familyRequestLiveData

    val familyId: LiveData<String>
        get() = _familyId

    fun createFamily(profile: CreateFamilyReq, imageFile : File?) = viewModelScope.launch {
        _familyRequestLiveData.postValue(Resource.loading(null))
        _familyRequestLiveData.postValue(familyRepository.createFamily(profile, imageFile))
    }

    fun joinFamily(profile: JoinFamilyReq) = viewModelScope.launch {
        _familyRequestLiveData.postValue(Resource.loading(null))
        _familyRequestLiveData.postValue(familyRepository.joinFamily(profile))
    }

    fun checkFamilyCode(familyCode: String) = viewModelScope.launch {
        _familyId.postValue(Resource.loading(null).toString())
//        _familyId.postValue(familyRepository.checkFamilyCode(familyCode).data.toString())
        Log.d(TAG, "StartSettingViewModel - checkFamilyCode() response ${
            familyRepository.checkFamilyCode(
                familyCode
            ).data?.dataset?.familyId
        }")
    }
}