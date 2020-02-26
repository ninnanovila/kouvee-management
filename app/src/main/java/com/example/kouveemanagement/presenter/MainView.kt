package com.example.kouveemanagement.presenter

import com.example.kouveemanagement.model.*
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
    fun showProgress()
    fun hideProgress()
}

interface CustomerView: CustomerRepositoryCallback<CustomerResponse> {
    fun showLoading()
    fun hideLoading()
}

interface PetSizeView: PetSizeRepositoryCallback<PetSizeResponse> {
    fun showLoading()
    fun hideLoading()
}

interface PetTypeView: PetTypeRepositoryCallback<PetTypeResponse> {
    fun showLoading()
    fun hideLoading()
}

interface SupplierView: SupplierRepositoryCallback<SupplierResponse> {
    fun showLoading()
    fun hideLoading()
}