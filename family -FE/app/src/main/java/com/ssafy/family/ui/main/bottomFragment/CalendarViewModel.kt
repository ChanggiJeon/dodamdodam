package com.ssafy.family.ui.main.bottomFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.family.data.remote.res.ScheduleRes
import com.ssafy.family.data.repository.CalendarRepository
import com.ssafy.family.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(private val calendarRepository: CalendarRepository) :
    ViewModel() {

    private val _getDayRequestLiveData = MutableLiveData<Resource<ScheduleRes>>()
    val getDayRequestLiveData: LiveData<Resource<ScheduleRes>>
        get() = _getDayRequestLiveData

    private val _getMonthRequestLiveData = MutableLiveData<Resource<ScheduleRes>>()
    val getMonthRequestLiveData: LiveData<Resource<ScheduleRes>>
        get() = _getMonthRequestLiveData

    fun getDaySchedule(day: String) = viewModelScope.launch {
        _getDayRequestLiveData.postValue(Resource.loading(null))
        _getDayRequestLiveData.postValue(calendarRepository.getDaySchedule(day))
    }

    fun getMonthSchedule(month: String) = viewModelScope.launch {
        _getMonthRequestLiveData.postValue(Resource.loading(null))
        _getMonthRequestLiveData.postValue(calendarRepository.getMonthSchedule(month))
    }
}