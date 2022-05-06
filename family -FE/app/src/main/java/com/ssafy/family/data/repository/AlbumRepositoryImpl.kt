package com.ssafy.family.data.repository


import android.util.Log
import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.api.AlbumAPI
import com.ssafy.family.data.remote.req.AddAlbumReq
import com.ssafy.family.data.remote.req.AlbumReactionReq
import com.ssafy.family.data.remote.res.AlbumDetailRes
import com.ssafy.family.data.remote.res.AlbumRes
import com.ssafy.family.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AlbumRepositoryImpl(
    private val api: AlbumAPI,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher
) : AlbumRepository {
    override suspend fun findAllAlbum(): Resource<AlbumRes> = withContext(ioDispatcher) {
        try {
            val response = api.allAlbum()
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                response.code() == 403 -> {
                    Resource.expired(response.body()!!)
                }
                else -> {
                    Resource.error(null, response.message())
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버와 연결오류")
        }
    }

    override suspend fun detailAlbum(albumId: Int): Resource<AlbumDetailRes> =
        withContext(ioDispatcher) {
            try {
                val response = api.detailAlbum(albumId)
                when {
                    response.isSuccessful -> {
                        Resource.success(response.body()!!)
                    }
                    response.code() == 403 -> {
                        Resource.expired(response.body()!!)
                    }
                    else -> {
                        Resource.error(null, response.message())
                    }
                }
            } catch (e: Exception) {
                Resource.error(null, "서버와 연결오류")
            }
        }

    override suspend fun deleteReaction(reactionId: Int): Resource<BaseResponse> =
        withContext(ioDispatcher) {
            try {
                val response = api.deleteReaction(reactionId)
                when {
                    response.isSuccessful -> {
                        Resource.success(response.body()!!)
                    }
                    response.code() == 403 -> {
                        Resource.expired(response.body()!!)
                    }
                    else -> {
                        Resource.error(null, response.message())
                    }
                }
            } catch (e: Exception) {
                Resource.error(null, "서버와 연결오류")
            }
        }

    override suspend fun addReaction(albumReactionReq: AlbumReactionReq): Resource<BaseResponse> =
        withContext(ioDispatcher) {
            try {
                val response = api.addReaction(albumReactionReq)
                when {
                    response.isSuccessful -> {
                        Resource.success(response.body()!!)
                    }
                    response.code() == 403 -> {
                        Resource.expired(response.body()!!)
                    }
                    else -> {
                        Resource.error(null, response.message())
                    }
                }
            } catch (e: Exception) {
                Resource.error(null, "서버와 연결오류")
            }
        }

    override suspend fun searchAlbum(keyword: String): Resource<AlbumRes> =
        withContext(ioDispatcher) {
            try {
                val response = api.searchAlbum(keyword)
                when {
                    response.isSuccessful -> {
                        Resource.success(response.body()!!)
                    }
                    response.code() == 403 -> {
                        Resource.expired(response.body()!!)
                    }
                    else -> {
                        Resource.error(null, response.message())
                    }
                }
            } catch (e: Exception) {
                Resource.error(null, "서버와 연결오류")
            }
        }

    override suspend fun addAlbum(
        addAlbum: AddAlbumReq,
        imagefiles: ArrayList<MultipartBody.Part>
    ): Resource<BaseResponse> =
        withContext(ioDispatcher) {
//            try {
                val map = HashMap<String,RequestBody>()
                var data = ""
                for(i in addAlbum.hashTags.indices){
                    if(i!=0){
                        data+=","
                    }
                    data+=addAlbum.hashTags[i].text
                }
                map.put("hashTags",getBody(data))
                map.put("date",getBody(addAlbum.date))
                map.put("mainIndex",getBody(addAlbum.mainIndex))
                Log.d("ddddd", "addAlbum: "+map)
                val response =
                    api.addAlbum(data = map, imagefiles)
                when {
                    response.isSuccessful -> {
                        Resource.success(response.body()!!)
                    }
                    response.code() == 403 -> {
                        Resource.expired(response.body()!!)
                    }
                    else -> {
                        Log.d("ddddd", "addAlbum: err")
                        Resource.error(null, response.message())
                    }
                }
//            } catch (e: Exception) {
//                Log.d("ddddd", "addAlbum: exception")
//                Resource.error(null, "서버와 연결오류")
//            }
        }

    override suspend fun seachDateAlbum(date: String): Resource<AlbumRes> =
        withContext(ioDispatcher) {
        withContext(ioDispatcher) {
            try {
                val response = api.seachDateAlbum(date)
                when {
                    response.isSuccessful -> {
                        Resource.success(response.body()!!)
                    }
                    response.code() == 403 -> {
                        Resource.expired(response.body()!!)
                    }
                    else -> {
                        Resource.error(null, response.message())
                    }
                }
            } catch (e: Exception) {
                Resource.error(null, "서버와 연결오류")
            }
        }
    }

    private fun getBody(value: Any): RequestBody{
        return value.toString().toRequestBody("text/plain".toMediaTypeOrNull());
    }

    private fun getArrayBody(value: List<Any>): ArrayList<RequestBody> {
        val list = arrayListOf<RequestBody>()
        value.forEach { list.add(value.toString().toRequestBody("text/plain".toMediaTypeOrNull())) }
        return list
    }

    private fun getImageBody(name: String, file: List<File>): ArrayList<MultipartBody.Part> {
        val list = arrayListOf<MultipartBody.Part>()
        file.forEach {
            list.add(
                MultipartBody.Part.createFormData(
                    name = name,
                    filename = it.name,
                    body = it.asRequestBody("image/*".toMediaType())
                )
            )
        }
        return list
    }


}