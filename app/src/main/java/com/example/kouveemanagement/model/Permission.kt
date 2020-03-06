package com.example.kouveemanagement.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Permission(
    @SerializedName("id_transaction")
    var id_transaction: String? = null,
    @SerializedName("id_employee")
    var id_employee: String? = null
) : Parcelable

data class PermissionResponse(
    var status: String,
    @SerializedName("result")
    var permissions: List<Permission>
)

