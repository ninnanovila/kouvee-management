package com.example.kouveemanagement.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Service(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("id_size")
    var id_size: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("price")
    var price: Double? = null,
    @SerializedName("created_at")
    var created_at: String? = null,
    @SerializedName("updated_at")
    var updated_at: String? = null,
    @SerializedName("deleted_at")
    var deleted_at: String? = null
) : Parcelable


data class ServiceResponse(
    val status: String,
    @SerializedName("result")
    val services: List<Service>
)