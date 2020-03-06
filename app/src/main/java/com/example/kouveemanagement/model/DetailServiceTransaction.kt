package com.example.kouveemanagement.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailServiceTransaction(
    @SerializedName("id_transaction")
    var id_transaction: String? = null,
    @SerializedName("id_service")
    var id_service: String? = null,
    @SerializedName("quantity")
    var quantity: Int? = null,
    @SerializedName("subtotal_price")
    var subtotal_price: Double? = null
) : Parcelable

data class DetailServiceTransactionResponse(
    var status: String,
    @SerializedName("result")
    val detailServiceTransactions: List<DetailServiceTransaction>
)