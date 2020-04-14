package com.example.kouveemanagement.repository

interface LoginRepositoryCallback<LoginResponse> {
    fun loginSuccess(data: LoginResponse?)
    fun loginFailed()
}

interface EmployeeRepositoryCallback<EmployeeResponse> {
    fun employeeSuccess(data: EmployeeResponse?)
    fun employeeFailed()
}

interface ProductRepositoryCallback<ProductResponse> {
    fun productSuccess(data: ProductResponse?)
    fun productFailed()
}

interface UploadPhotoProductRepositoryCallback<ResponseBody> {
    fun uploadProductSuccess(data: ResponseBody?)
    fun uploadProductFailed()
}

interface CustomerRepositoryCallback<CustomerResponse> {
    fun customerSuccess(data: CustomerResponse?)
    fun customerFailed()
}

interface PetSizeRepositoryCallback<PetSizeResponse> {
    fun petSizeSuccess(data: PetSizeResponse?)
    fun petSizeFailed()
}

interface PetTypeRepositoryCallback<PetTypeResponse> {
    fun petTypeSuccess(data: PetTypeResponse?)
    fun petTypeFailed()
}

interface SupplierRepositoryCallback<SupplierResponse> {
    fun supplierSuccess(data: SupplierResponse?)
    fun supplierFailed()
}

interface ServiceRepositoryCallback<ServiceResponse> {
    fun serviceSuccess(data: ServiceResponse?)
    fun serviceFailed()
}

interface CustomerPetRepositoryCallback<CustomerPetResponse> {
    fun customerPetSuccess(data: CustomerPetResponse?)
    fun customerPetFailed()
}

interface OrderProductRepositoryCallback<OrderProductResponse> {
    fun orderProductSuccess(data: OrderProductResponse?)
    fun orderProductFailed()
}

interface DetailOrderProductRepositoryCallback<DetailOrderProductResponse> {
    fun detailOrderProductSuccess(data: DetailOrderProductResponse?)
    fun detailOrderProductFailed()
}

interface TransactionRepositoryCallback<TransactionResponse>{
    fun transactionSuccess(data: TransactionResponse?)
    fun transactionFailed()
}

interface DetailProductTransactionRepositoryCallback<DetailProductTransactionResponse>{
    fun detailProductTransactionSuccess(data: DetailProductTransactionResponse?)
    fun detailProductTransactionFailed()
}

interface DetailServiceTransactionRepositoryCallback<DetailServiceTransactionResponse>{
    fun detailServiceTransactionSuccess(data: DetailServiceTransactionResponse?)
    fun detailServiceTransactionFailed()
}

interface MinProductRepositoryCallback<ProductResponse> {
    fun minProductSuccess(data: ProductResponse?)
    fun minProductFailed()
}