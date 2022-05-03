package com.ssafy.family.ui.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.AlbumReactionReq
import com.ssafy.family.data.remote.res.AlbumDetailRes
import com.ssafy.family.data.remote.res.AllAlbum
import com.ssafy.family.data.repository.AlbumRepository
import com.ssafy.family.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailAlbumViewModel @Inject constructor(private val albumRepository: AlbumRepository) :
    ViewModel() {
    private val _titleLiveData = MutableLiveData<String>()
    val titleLiveData: LiveData<String>
        get() = _titleLiveData

    private val _bottombuttonLeftLivedate = MutableLiveData<String>()
    val bottombuttonLeftLivedate: LiveData<String>
        get() = _bottombuttonLeftLivedate

    private val _bottombuttonRightLivedate = MutableLiveData<String>()
    val bottombuttonRightLivedate: LiveData<String>
        get() = _bottombuttonRightLivedate

    private val _saveAlbumRequestLiveData = MutableLiveData<AllAlbum>()
    val saveAlbumLiveData: LiveData<AllAlbum>
        get() = _saveAlbumRequestLiveData

    private val _detailAlbumRequestLiveData = MutableLiveData<Resource<AlbumDetailRes>>()
    val detailAlbumRequestLiveData: LiveData<Resource<AlbumDetailRes>>
        get() = _detailAlbumRequestLiveData

    private val _deleteReactionRequestLiveData = MutableLiveData<Resource<BaseResponse>>()
    val deleteReactionRequestLiveData: LiveData<Resource<BaseResponse>>
        get() = _deleteReactionRequestLiveData

    private val _addReactionRequestLiveData = MutableLiveData<Resource<BaseResponse>>()
    val addReactionRequestLiveData: LiveData<Resource<BaseResponse>>
        get() = _addReactionRequestLiveData


    fun setBottomButton(left: String, right: String) = viewModelScope.launch {
        _bottombuttonLeftLivedate.value = left
        _bottombuttonRightLivedate.value = right
    }

    fun setTitle(title: String) = viewModelScope.launch {
        _titleLiveData.value = title
    }

    fun setSaveAlbum(allAlbum: AllAlbum) = viewModelScope.launch {
        _saveAlbumRequestLiveData.value = allAlbum
    }

    fun detailAlbum(albumId: Int) = viewModelScope.launch {
        _detailAlbumRequestLiveData.postValue(Resource.loading(null))
        _detailAlbumRequestLiveData.postValue(albumRepository.detailAlbum(albumId))
    }

    fun deleteReaction(reactionId: Int) = viewModelScope.launch {
        _deleteReactionRequestLiveData.postValue(Resource.loading(null))
        _deleteReactionRequestLiveData.postValue(albumRepository.deleteReaction(reactionId))
    }

    fun addReaction(albumReactionReq: AlbumReactionReq)=viewModelScope.launch {
        _addReactionRequestLiveData.postValue(Resource.loading(null))
        _addReactionRequestLiveData.postValue(albumRepository.addReaction(albumReactionReq))
    }
}