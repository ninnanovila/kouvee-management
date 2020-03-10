package com.example.kouveemanagement.presenter

import com.example.kouveemanagement.model.*
import com.example.kouveemanagement.repository.*
import okhttp3.ResponseBody

interface LoginView : LoginRepositoryCallback<LoginResponse> {
    fun showLoginLoading()
    fun hideLoginLoading()
}

interface EmployeeView : EmployeeRepositoryCallback<EmployeeResponse> {
    fun showEmployeeLoading()
    fun hideEmployeeLoading()
}

interface ProductView : ProductRepositoryCallback<ProductResponse> {
    fun showProductLoading()
    fun hideProductLoading()
}

interface UploadPhotoProductView : UploadPhotoProductRepositoryCallback<ResponseBody> {
    fun showUploadProgress()
    fun hideUploadProgress()
}

interface CustomerView: CustomerRepositoryCallback<CustomerResponse> {
    fun showCustomerLoading()
    fun hideCustomerLoading()
}

interface PetSizeView: PetSizeRepositoryCallback<PetSizeResponse> {
    fun showPetSizeLoading()
    fun hidePetSizeLoading()
}

interface PetTypeView: PetTypeRepositoryCallback<PetTypeResponse> {
    fun showPetTypeLoading()
    fun hidePetTypeLoading()
}

interface SupplierView: SupplierRepositoryCallback<SupplierResponse> {
    fun showSupplierLoading()
    fun hideSupplierLoading()
}

interface ServiceView: ServiceRepositoryCallback<ServiceResponse> {
    fun showServiceLoading()
    fun hideServiceLoading()
}

interface CustomerPetView: CustomerPetRepositoryCallback<CustomerPetResponse> {
    fun showCustomerPetLoading()
    fun hideCustomerPetLoading()
}

interface OrderProductView: OrderProductRepositoryCallback<OrderProductResponse> {
    fun showOrderProductLoading()
    fun hideOrderProductLoading()
}

interface DetailOrderProductView: DetailOrderProductRepositoryCallback<DetailOrderProductResponse> {
    fun showDetailOrderProductLoading()
    fun hideDetailOrderProductLoading()
}

interface TransactionView: TransactionRepositoryCallback<TransactionResponse> {
    fun showTransactionLoading()
    fun hideTransactionLoading()
}

interface DetailProductTransactionView: DetailProductTransactionRepositoryCallback<DetailProductTransactionResponse> {
    fun showDetailProductTransactionLoading()
    fun hideDetailProductTransactionLoading()
}

interface DetailServiceTransactionView: DetailServiceTransactionRepositoryCallback<DetailServiceTransactionResponse> {
    fun showDetailServiceTransactionLoading()
    fun hideDetailServiceTransactionLoading()
}