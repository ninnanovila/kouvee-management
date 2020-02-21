package com.example.kouveemanagement.presenter

import com.example.kouveemanagement.model.EmployeeResponse
import com.example.kouveemanagement.model.LoginResponse
import com.example.kouveemanagement.model.ProductResponse
import com.example.kouveemanagement.repository.EmployeeRepositoryCallback
import com.example.kouveemanagement.repository.LoginRepositoryCallback
import com.example.kouveemanagement.repository.UploadPhotoProductRepositoryCallback
import com.example.kouveemanagement.repository.ProductRepositoryCallback
import okhttp3.ResponseBody

interface LoginView : LoginRepositoryCallback<LoginResponse> {
    fun showLoading()
    fun hideLoading()
}

interface EmployeeView : EmployeeRepositoryCallback<EmployeeResponse> {
    fun showLoading()
    fun hideLoading()
}

interface ProductView : ProductRepositoryCallback<ProductResponse> {
    fun showLoading()
    fun hideLoading()
}

interface UploadPhotoProductView : UploadPhotoProductRepositoryCallback<ResponseBody> {
    fun showLoading()
    fun hideLoading()
}