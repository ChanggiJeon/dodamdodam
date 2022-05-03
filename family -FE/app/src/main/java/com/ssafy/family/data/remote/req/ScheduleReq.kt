package com.ssafy.family.data.remote.req

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate

@Parcelize
data class ScheduleReq(
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("startDate")
    val startDate: LocalDate,
    @SerializedName("endDate")
    val endDate: LocalDate
) : Parcelable

