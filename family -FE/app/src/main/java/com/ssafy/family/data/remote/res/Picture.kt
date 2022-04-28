package com.ssafy.family.data.remote.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Picture(
    @SerializedName("id")
    val id: Int,
    @SerializedName("album")
    val album: List<Album>,
    @SerializedName("origin_name")
    val origin_name: String,
    @SerializedName("path_name")
    val path_name: String,
    @SerializedName("is_main")
    val is_main: Boolean
): Parcelable
