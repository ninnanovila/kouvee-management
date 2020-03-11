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
    var status: String? = null,
    @SerializedName("created_at")
    var created_at: String? = null,
    @SerializedName("updated_at")
    var updated_at: String? = null,
    @SerializedName("last_cs")
    var last_cs: String? = null,
    @SerializedName("last_cr")
    var last_cr: String? = null
) : Parcelable

data class TransactionResponse(
    var status: String,
    @SerializedName("result")
    var transactions: List<Transaction>
)