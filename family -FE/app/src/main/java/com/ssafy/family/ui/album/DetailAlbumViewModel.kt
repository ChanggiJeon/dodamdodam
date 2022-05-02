package com.ssafy.family.ui.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssafy.family.data.remote.res.AlbumRes
import com.ssafy.family.data.remote.res.AllAlbum
import com.ssafy.family.data.repository.AlbumRepository
import com.ssafy.family.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class DetailAlbumViewModel @Inject constructor(private val albumRepository: AlbumRepository) :
    ViewModel() {
    private val _detailAlbumRequestLiveData = MutableLiveData<AllAlbum>()
    val detailAlbumLiveData: LiveData<AllAlbum>
        get() = _detailAlbumRequestLiveData

    fun setDetailAlbum(allAlbum: AllAlbum){
        _detailAlbumRequestLiveData.postValue(allAlbum)
    }
}