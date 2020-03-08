package com.example.kouveemanagement.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailOrderProduct (
    @SerializedName("id_order")
    var id_order: String? = null,
    @SerializedName("id_product")
    var id_product: String? = null,
    @SerializedName("quantity")
    var quantity: Int? = null,
    @SerializedName("subtotal")
    var subtotal: Double? = null
) : Parcelable

data class DetailOrderProductResponse(
    val status: String,
    @SerializedName("result")
    val detailOrderProducts: List<DetailOrderProduct>
)