package com.example.kouveemanagement.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PetType(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null
): Parcelable

data class PetTypeResponse(
    val status: String,
    @SerializedName("result")
    val pettype: List<PetType>
)