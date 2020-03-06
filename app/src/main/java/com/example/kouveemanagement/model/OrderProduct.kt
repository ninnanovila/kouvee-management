package com.example.kouveemanagement.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderProduct(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("id_supplier")
    var id_supplier: String? = null,
    @SerializedName("total")
    var total: Double? = null,
    @SerializedName("status")
    var status: String? = null,
    @SerializedName("printed_at")
    var printed_at: String? = null,
    @SerializedName("created_at")
    var created_at: String? = null,
    @SerializedName("updated_at")
    var updated_at: String? = null
) : Parcelable

data class OrderProductResponse(
    val status: String,
    @SerializedName("result")
    val orderProducts: List<OrderProduct>
)
