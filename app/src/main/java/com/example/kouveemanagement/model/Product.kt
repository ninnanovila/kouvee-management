package com.example.kouveemanagement.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("unit")
    var unit: String? = null,
    @SerializedName("stock")
    var stock: Int? = null,
    @SerializedName("min_stock")
    var min_stock: Int? = null,
    @SerializedName("price")
    var price: Double? = null,
    @SerializedName("photo")
    var photo: String? = null,
    @SerializedName("last_emp")
    var last_emp: String? = null
): Parcelable

data class ProductResponse(
    @SerializedName("result")
    val products: List<Product>
)