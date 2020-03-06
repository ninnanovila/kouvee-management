package com.example.kouveemanagement.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Transaction(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("id_customer_pet")
    var id_customer_pet: String? = null,
    @SerializedName("total_price")
    var total_price: Double? = null,
    @SerializedName("discount")
    var discount: String? = null,
    @SerializedName("payment")
    var payment: String? = null,
    @SerializedName("status")
    var status: String? = null
) : Parcelable

data class TransactionResponse(
    var status: String,
    @SerializedName("result")
    var transactions: List<Transaction>
)