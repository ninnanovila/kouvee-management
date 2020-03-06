package com.example.kouveemanagement.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailProductTransaction(
    @SerializedName("id_transaction")
    var id_transaction: String? = null,
    @SerializedName("id_product")
    var id_product: String? = null,
    @SerializedName("quantity")
    var quantity: Int? = null,
    @SerializedName("subtotal_price")
    var subtotal_price: Double? = null
) : Parcelable

data class DetailProductTransactionResponse(
    val status: String,
    @SerializedName("result")
    val detailProductTransactions: List<DetailProductTransaction>
)

