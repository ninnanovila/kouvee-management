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
    @SerializedName("created_at")
    var created_at: String? = null,
    @SerializedName("updated_at")
    var updated_at: String? = null,
    @SerializedName("deleted_at")
    var deleted_at: String? = null
): Parcelable

data class ProductResponse(
    val status: String,
    @SerializedName("result")
    val products: List<Product>
)