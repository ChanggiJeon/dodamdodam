package com.ssafy.family.util

import com.ssafy.family.config.ApplicationClass
import com.ssafy.family.data.UserInfo

object LoginUtil {
    val ACCESS_TOKEN = "accessToken"
    val REFRESH_TOKEN = "refreshToken"
    val NAME = "name"
    val PROFILE_ID = "profileId"
    val FAMILY_ID = "familyId"

    private val preferences = ApplicationClass.sSharedPreferences

    fun isAutoLogin(): Boolean {
        return preferences.getAutoLoginFlag()
    }

    fun signOut() {
        preferences.deleteString(ApplicationClass.JWT)
        deleteUserInfo()
    }

    fun setAutoLogin(flag: Boolean) {
        preferences.setAutoLogin(flag)
    }

    fun isTokenExisted(): Boolean {
        return !preferences.getString(ApplicationClass.JWT).isNullOrBlank()
    }

    fun saveUserInfo(userInfo: UserInfo) {

        preferences.setString(ACCESS_TOKEN, userInfo.accessToken)
        preferences.setString(REFRESH_TOKEN, userInfo.refreshToken)
        preferences.setString(NAME, userInfo.name)
        preferences.setString(PROFILE_ID, userInfo.profileId.toString())
        preferences.setString(FAMILY_ID, userInfo.familyId.toString())
    }

    fun deleteUserInfo() {
        preferences.deleteString(ACCESS_TOKEN)
        preferences.deleteString(REFRESH_TOKEN)
        preferences.deleteString(NAME)
        preferences.deleteString(PROFILE_ID)
        preferences.deleteString(FAMILY_ID)
    }

    fun getUserInfo(): UserInfo? {
        val accessToken = preferences.getString(ACCESS_TOKEN)
        val refreshToken = preferences.getString(REFRESH_TOKEN)
        val name = preferences.getString(NAME)
        val profileId = preferences.getString(PROFILE_ID)?.toLong()
        val familyId = preferences.getString(FAMILY_ID)?.toLong()

        return if (accessToken.isNullOrBlank() or refreshToken.isNullOrBlank() or name.isNullOrBlank() or (profileId == null) or (familyId == null)) {
            null
        } else {
            UserInfo(accessToken!!, refreshToken!!, name!!, profileId!!, familyId!!)
        }
    }
}