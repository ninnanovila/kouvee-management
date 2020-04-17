package com.example.kouveemanagement.repository

interface LoginRepositoryCallback<LoginResponse> {
    fun loginSuccess(data: LoginResponse?)
    fun loginFailed(data: String)
}

interface EmployeeRepositoryCallback<EmployeeResponse> {
    fun employeeSuccess(data: EmployeeResponse?)
    fun employeeFailed(data: String)
}

interface ServiceRepositoryCallback<ServiceResponse> {
    fun serviceSuccess(data: ServiceResponse?)
    fun serviceFailed(data: String)
}

interface CustomerRepositoryCallback<CustomerResponse> {
    fun customerSuccess(data: CustomerResponse?)
    fun customerFailed(data: String)
}

interface ProductRepositoryCallback<ProductResponse> {
    fun productSuccess(data: ProductResponse?)
    fun productFailed(data: String)
}

interface UploadPhotoProductRepositoryCallback<ResponseBody> {
    fun uploadProductSuccess(data: ResponseBody?)
    fun uploadProductFailed(data: String)
}

interface PetSizeRepositoryCallback<PetSizeResponse> {
    fun petSizeSuccess(data: PetSizeResponse?)
    fun petSizeFailed(data: String)
}

interface PetTypeRepositoryCallback<PetTypeResponse> {
    fun petTypeSuccess(data: PetTypeResponse?)
    fun petTypeFailed(data: String)
}

interface SupplierRepositoryCallback<SupplierResponse> {
    fun supplierSuccess(data: SupplierResponse?)
    fun supplierFailed(data: String)
}

interface OrderProductRepositoryCallback<OrderProductResponse> {
    fun orderProductSuccess(data: OrderProductResponse?)
    fun orderProductFailed(data: String)
}

interface DetailOrderProductRepositoryCallback<DetailOrderProductResponse> {
    fun detailOrderProductSuccess(data: DetailOrderProductResponse?)
    fun detailOrderProductFailed(data: String)
}

interface CustomerPetRepositoryCallback<CustomerPetResponse> {
    fun customerPetSuccess(data: CustomerPetResponse?)
    fun customerPetFailed(data: String)
}

interface TransactionRepositoryCallback<TransactionResponse>{
    fun transactionSuccess(data: TransactionResponse?)
    fun transactionFailed(data: String)
}

interface DetailProductTransactionRepositoryCallback<DetailProductTransactionResponse>{
    fun detailProductTransactionSuccess(data: DetailProductTransactionResponse?)
    fun detailProductTransactionFailed(data: String)
}

interface DetailServiceTransactionRepositoryCallback<DetailServiceTransactionResponse>{
    fun detailServiceTransactionSuccess(data: DetailServiceTransactionResponse?)
    fun detailServiceTransactionFailed(data: String)
}

interface MinProductRepositoryCallback<ProductResponse> {
    fun minProductSuccess(data: ProductResponse?)
    fun minProductFailed(data: String)
}