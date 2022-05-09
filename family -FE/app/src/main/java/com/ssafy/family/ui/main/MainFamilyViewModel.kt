package com.ssafy.family.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.family.data.remote.res.MissionRes
import com.ssafy.family.data.repository.MainFamilyRepository
import com.ssafy.family.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainFamilyViewModel @Inject constructor(private val mainFamilyRepository: MainFamilyRepository) :
    ViewModel() {

        private val _todayMissionRequestLiveData = MutableLiveData<Resource<MissionRes>>()
        val todayMissionRequestLiveData:LiveData<Resource<MissionRes>>
        get() = _todayMissionRequestLiveData

    fun getTodayMission() = viewModelScope.launch {
        _todayMissionRequestLiveData.postValue(Resource.loading(null))
        _todayMissionRequestLiveData.postValue(mainFamilyRepository.getTodayMission())
    }
}