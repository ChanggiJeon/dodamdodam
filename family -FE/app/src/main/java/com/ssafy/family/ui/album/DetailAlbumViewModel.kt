package com.ssafy.family.ui.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.res.Album
import com.ssafy.family.data.remote.res.AlbumDetailRes
import com.ssafy.family.data.remote.res.AlbumRes
import com.ssafy.family.data.remote.res.AllAlbum
import com.ssafy.family.data.repository.AlbumRepository
import com.ssafy.family.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailAlbumViewModel @Inject constructor(private val albumRepository: AlbumRepository) :
    ViewModel() {
    private val _saveAlbumRequestLiveData = MutableLiveData<AllAlbum>()
    val saveAlbumLiveData: LiveData<AllAlbum>
        get() = _saveAlbumRequestLiveData

    private val _detailAlbumRequestLiveData = MutableLiveData<Resource<AlbumDetailRes>>()
    val detailAlbumRequestLiveData: LiveData<Resource<AlbumDetailRes>>
        get() = _detailAlbumRequestLiveData

    private val _deleteReactionRequestLiveData = MutableLiveData<Resource<BaseResponse>>()
    val deleteReactionRequestLiveData: LiveData<Resource<BaseResponse>>
        get() = _deleteReactionRequestLiveData

    fun setSaveAlbum(allAlbum: AllAlbum) = viewModelScope.launch{
        _saveAlbumRequestLiveData.value=allAlbum
    }

    fun detailAlbum(albumId: Int) = viewModelScope.launch {
        _detailAlbumRequestLiveData.postValue(Resource.loading(null))
        _detailAlbumRequestLiveData.postValue(albumRepository.detailAlbum(albumId))
    }

    fun deleteReaction(reactionId:Int) =viewModelScope.launch {
        _deleteReactionRequestLiveData.postValue(Resource.loading(null))
        _deleteReactionRequestLiveData.postValue(albumRepository.deleteReaction(reactionId))
    }

}