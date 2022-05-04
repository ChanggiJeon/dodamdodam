package com.ssafy.family.data.repository


import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.api.AlbumAPI
import com.ssafy.family.data.remote.req.AddAlbumReq
import com.ssafy.family.data.remote.req.AlbumReactionReq
import com.ssafy.family.data.remote.res.AlbumDetailRes
import com.ssafy.family.data.remote.res.AlbumRes
import com.ssafy.family.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
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
        imagefiles: ArrayList<File>
    ): Resource<BaseResponse> =
        withContext(ioDispatcher) {
            try {
                val hashTags = getArrayBody("hashTags", addAlbum.hashTags)
                val date = getBody("date", addAlbum.date)
                val mainIndex = getBody("mainIndex", addAlbum.mainIndex)
                val multipartFiles = getImageBody("multipartFiles",imagefiles)
                val response =
                    api.addAlbum(hashTags = hashTags, date = date, mainIndex, multipartFiles)
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

    private fun getBody(name: String, value: Any): MultipartBody.Part {
        return MultipartBody.Part.createFormData(name, value.toString())
    }

    private fun getArrayBody(name: String, value: List<Any>): ArrayList<MultipartBody.Part> {
        val list = arrayListOf<MultipartBody.Part>()
        value.forEach { list.add(MultipartBody.Part.createFormData(name, it.toString())) }
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