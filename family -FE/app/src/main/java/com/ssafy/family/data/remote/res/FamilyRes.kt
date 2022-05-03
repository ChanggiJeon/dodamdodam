package com.ssafy.family.data.remote.res

import com.google.gson.annotations.SerializedName
import com.ssafy.family.config.BaseResponse

class FamilyRes: BaseResponse() {
    @SerializedName("data.familyId")
    val familyId: Int? = null
}