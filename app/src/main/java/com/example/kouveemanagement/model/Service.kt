package com.example.kouveemanagement.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Service(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("id_type")
    var id_type: String? = null,
    @SerializedName("id_size")
    var id_size: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("price")
    var price: Double? = null,
    @SerializedName("last_emp")
    var last_emp: String? = null
) : Parcelable


data class ServiceResponse(
    val status: String,
    @SerializedName("result")
    val services: List<Service>
)