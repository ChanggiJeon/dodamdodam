package com.ssafy.family.data.repository


import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.AddAlbumReq
import com.ssafy.family.data.remote.req.AlbumReactionReq
import com.ssafy.family.data.remote.res.AlbumDetailRes
import com.ssafy.family.data.remote.res.AlbumRes
import com.ssafy.family.util.Resource
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.File

interface AlbumRepository {
    suspend fun findAllAlbum(): Resource<AlbumRes>
    suspend fun detailAlbum(albumId:Int):Resource<AlbumDetailRes>
    suspend fun deleteReaction(reactionId: Int):Resource<BaseResponse>
    suspend fun addReaction(albumReactionReq: AlbumReactionReq):Resource<BaseResponse>
    suspend fun searchAlbum(keyword:String): Resource<AlbumRes>
    suspend fun addAlbum(addAlbum:AddAlbumReq, imagefiles: ArrayList<MultipartBody.Part>):Resource<BaseResponse>
    suspend fun seachDateAlbum(date: String): Resource<AlbumRes>
}