package com.ssafy.family.data.repository

import com.bumptech.glide.load.engine.Resource
import com.ssafy.family.data.remote.res.AlbumRes
import com.ssafy.family.data.remote.res.AllAlbum

interface AlbumRepository {
    suspend fun findAllAlbum():Resource<AlbumRes>
}