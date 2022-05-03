package com.ssafy.family.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ScheduleInfo(
    @SerializedName("scheduleId")
    val scheduleId: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("startDate")
    val startDate: Date,
    @SerializedName("endDate")
    val endDate: Date,
    @SerializedName("role")
    val role: String
): Parcelable
