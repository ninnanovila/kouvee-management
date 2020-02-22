package com.example.kouveemanagement.presenter

import com.example.kouveemanagement.model.CustomerResponse
import com.example.kouveemanagement.model.EmployeeResponse
import com.example.kouveemanagement.model.LoginResponse
import com.example.kouveemanagement.model.ProductResponse
import com.example.kouveemanagement.repository.*
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

interface CustomerView: CustomerRepositoryCallback<CustomerResponse> {
    fun showLoading()
    fun hideLoading()
}