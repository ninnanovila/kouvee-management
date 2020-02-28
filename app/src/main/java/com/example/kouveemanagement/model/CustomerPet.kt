package com.example.kouveemanagement.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CustomerPet(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("id_customer")
    var id_customer: String? = null,
    @SerializedName("id_type")
    var id_type: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("birthdate")
    var birthdate: String? = null,
    @SerializedName("created_at")
    var created_at: String? = null,
    @SerializedName("updated_at")
    var updated_at: String? = null,
    @SerializedName("deleted_at")
    var deleted_at: String? = null,
    @SerializedName("last_emp")
    var last_emp: String? = null
): Parcelable


data class CustomerPetResponse(
    val status: String,
    @SerializedName("result")
    val customerpets: List<CustomerPet>
)