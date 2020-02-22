package com.example.kouveemanagement.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Customer(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("address")
    var address: String? = null,
    @SerializedName("birthdate")
    var birthdate: String? = null,
    @SerializedName("phone_number")
    var phone_number: String? = null,
    @SerializedName("last_emp")
    var last_emp: String? = null
) : Parcelable


data class CustomerResponse(
    val status: String,
    @SerializedName("result")
    val customers: List<Customer>
)