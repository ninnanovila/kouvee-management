package com.example.kouveemanagement.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("result")
    val employee: Employee,
    @SerializedName("status")
    val status: String
)