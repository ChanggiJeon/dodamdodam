package com.ssafy.family.data.repository

import com.google.firebase.database.DatabaseReference
import com.ssafy.family.data.ChatData
import com.ssafy.family.data.remote.req.LoginReq
import com.ssafy.family.data.remote.res.LoginRes
import com.ssafy.family.util.Resource

interface ChatRepository {
    suspend fun send(data: ChatData, myRef: DatabaseReference): Any
    //회원가입
}