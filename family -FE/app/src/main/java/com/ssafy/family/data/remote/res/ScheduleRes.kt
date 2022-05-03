package com.ssafy.family.data.remote.res

import com.google.gson.annotations.SerializedName
import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.ScheduleInfo

class ScheduleRes: BaseResponse() {
    @SerializedName("scheduleInfo")
    val scheduleInfo: ScheduleInfo? = null
}