package com.ssafy.family.data.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ssafy.family.data.ChatData
import com.ssafy.family.data.remote.req.LoginReq
import com.ssafy.family.data.remote.api.AccountAPI
import com.ssafy.family.data.remote.res.LoginRes
import com.ssafy.family.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class ChatRepositoryImpl (
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher
) : ChatRepository{

    override suspend fun send(data: ChatData, myRef: DatabaseReference) = withContext(ioDispatcher){
         try{
             myRef.push().setValue(data)
        }catch (e:Exception){
            Resource.error(null,"서버와 연결오류")
        }
    }

}