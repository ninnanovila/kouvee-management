package com.example.kouveemanagement.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Employee(
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
    @SerializedName("role")
    var role: String? = null,
    @SerializedName("password")
    var password: String? = null,
    @SerializedName("created_at")
    var created_at: String? = null,
    @SerializedName("updated_at")
    var updated_at: String? = null,
    @SerializedName("deleted_at")
    var deleted_at: String? = null
) : Parcelable


data class EmployeeResponse(
    val status: String,
    @SerializedName("result")
    val employees: List<Employee>
)