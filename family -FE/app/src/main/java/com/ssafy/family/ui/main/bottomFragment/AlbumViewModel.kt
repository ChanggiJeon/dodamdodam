package com.ssafy.family.ui.main.bottomFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.family.data.remote.res.AlbumRes
import com.ssafy.family.data.repository.AccountRepository
import com.ssafy.family.data.repository.AlbumRepository
import com.ssafy.family.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(private val albumRepository: AlbumRepository) :
    ViewModel() {
    private val _allAlbumRequestLiveData = MutableLiveData<Resource<AlbumRes>>()
    val allAlbumRequestLiveData : LiveData<Resource<AlbumRes>>
        get() = _allAlbumRequestLiveData

    fun findAllAlbum() = viewModelScope.launch {
        _allAlbumRequestLiveData.postValue(Resource.loading(null))
        _allAlbumRequestLiveData.postValue(albumRepository.findAllAlbum())
    }
}