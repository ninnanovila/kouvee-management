package com.example.kouveemanagement.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PetType(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("created_at")
    var created_at: String? = null,
    @SerializedName("updated_at")
    var updated_at: String? = null,
    @SerializedName("deleted_at")
    var deleted_at: String? = null
): Parcelable

data class PetTypeResponse(
    val status: String,
    @SerializedName("result")
    val pettype: List<PetType>
)