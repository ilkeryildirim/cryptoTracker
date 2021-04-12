package com.ilkeryildirim.cryptracker.api.model.coin


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Roi(
    @SerializedName("currency")
    var currency: String?,
    @SerializedName("percentage")
    var percentage: Double?,
    @SerializedName("times")
    var times: Double?
):Parcelable