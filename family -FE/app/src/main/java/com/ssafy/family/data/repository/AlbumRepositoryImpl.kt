package com.ssafy.family.data.repository


import com.ssafy.family.data.remote.api.AlbumAPI
import com.ssafy.family.data.remote.res.AlbumRes
import com.ssafy.family.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.Exception

class AlbumRepositoryImpl(private val api: AlbumAPI,
                          private val ioDispatcher: CoroutineDispatcher,
                          private val mainDispatcher: CoroutineDispatcher
) :AlbumRepository {
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
                    Resource.error(null,response.message())
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버와 연결오류")
        }
    }
}