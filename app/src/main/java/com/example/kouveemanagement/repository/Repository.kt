package com.example.kouveemanagement.repository

import android.util.Log
import com.example.kouveemanagement.api.ApiClient
import com.example.kouveemanagement.model.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository {

//    LOGIN
    fun loginPost(id: String, password: String, callback: LoginRepositoryCallback<LoginResponse>) {

        ApiClient().services.loginPost(id, password).enqueue(object : Callback<LoginResponse?> {
            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                callback.loginFailed()
            }

            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                if (response.isSuccessful){
                    callback.loginSuccess(response.body())
                }else{
                    callback.loginFailed()
                }
            }
        })
    }

//    EMPLOYEE
    fun getAllEmployee(callback: EmployeeRepositoryCallback<EmployeeResponse>) {
        ApiClient().services.getAllEmployee().enqueue(object : Callback<EmployeeResponse?> {
            override fun onFailure(call: Call<EmployeeResponse?>, t: Throwable) {
                callback.employeeFailed()
            }

            override fun onResponse(
                call: Call<EmployeeResponse?>,
                response: Response<EmployeeResponse?>
            ) {
                if (response.isSuccessful){
                    callback.employeeSuccess(response.body())
                }else{
                    callback.employeeFailed()
                }
            }

        })
    }

    fun getEmployeeBySearch(query: String, callback: EmployeeRepositoryCallback<EmployeeResponse>){
        ApiClient().services.getEmployeeBySearch(query).enqueue(object : Callback<EmployeeResponse?> {
            override fun onFailure(call: Call<EmployeeResponse?>, t: Throwable) {
                callback.employeeFailed()
            }

            override fun onResponse(
                call: Call<EmployeeResponse?>,
                response: Response<EmployeeResponse?>
            ) {
                if (response.isSuccessful){
                    callback.employeeSuccess(response.body())
                }else{
                    callback.employeeFailed()
                }
            }
        })
    }

    fun addEmployee(employee: Employee, callback: EmployeeRepositoryCallback<EmployeeResponse>) {

        ApiClient().services.addEmployee(employee).enqueue(object : Callback<EmployeeResponse?> {
            override fun onFailure(call: Call<EmployeeResponse?>, t: Throwable) {
                callback.employeeFailed()
            }

            override fun onResponse(
                call: Call<EmployeeResponse?>,
                response: Response<EmployeeResponse?>
            ) {
                if (response.isSuccessful){
                    callback.employeeSuccess(response.body())
                }else{
                    callback.employeeFailed()
                }
            }
        })
    }

    fun editEmployee(id: String, employee: Employee, callback: EmployeeRepositoryCallback<EmployeeResponse>) {

        ApiClient().services.editEmployee(id, employee).enqueue(object : Callback<EmployeeResponse?> {
            override fun onFailure(call: Call<EmployeeResponse?>, t: Throwable) {
                Log.d("FAILED", "WHY?", t)
                callback.employeeFailed()
            }

            override fun onResponse(
                call: Call<EmployeeResponse?>,
                response: Response<EmployeeResponse?>
            ) {
                if (response.isSuccessful){
                    callback.employeeSuccess(response.body())
                }else{
                    callback.employeeFailed()
                }
            }
        })
    }

    fun deleteEmployee(id: String, callback: EmployeeRepositoryCallback<EmployeeResponse>) {

        ApiClient().services.deleteEmployee(id).enqueue(object : Callback<EmployeeResponse?> {
            override fun onFailure(call: Call<EmployeeResponse?>, t: Throwable) {
                callback.employeeFailed()
            }

            override fun onResponse(
                call: Call<EmployeeResponse?>,
                response: Response<EmployeeResponse?>
            ) {
                if (response.isSuccessful){
                    callback.employeeSuccess(response.body())
                }else{
                    callback.employeeFailed()
                }
            }
        })
    }

//    PRODUCT
    fun getAllProduct(callback: ProductRepositoryCallback<ProductResponse>) {
        ApiClient().services.getAllProduct().enqueue(object : Callback<ProductResponse?> {
            override fun onFailure(call: Call<ProductResponse?>, t: Throwable) {
                callback.productFailed()
            }

            override fun onResponse(
                call: Call<ProductResponse?>,
                response: Response<ProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.productSuccess(response.body())
                }else{
                    callback.productFailed()
                }
            }
        })
    }

    fun getProductBySearch(query: String, callback: ProductRepositoryCallback<ProductResponse>){
        ApiClient().services.getProductBySearch(query).enqueue(object : Callback<ProductResponse?>{
            override fun onFailure(call: Call<ProductResponse?>, t: Throwable) {
                callback.productFailed()
            }

            override fun onResponse(
                call: Call<ProductResponse?>,
                response: Response<ProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.productSuccess(response.body())
                }else{
                    callback.productFailed()
                }
            }
        })
    }

    fun addProduct(product: Product, callback: ProductRepositoryCallback<ProductResponse>) {
        ApiClient().services.addProduct(product).enqueue(object : Callback<ProductResponse?> {
            override fun onFailure(call: Call<ProductResponse?>, t: Throwable) {
                callback.productFailed()
            }

            override fun onResponse(
                call: Call<ProductResponse?>,
                response: Response<ProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.productSuccess(response.body())
                }else{
                    callback.productFailed()
                }
            }
        })
    }

    fun editProduct(id: String, product: Product, callback: ProductRepositoryCallback<ProductResponse>) {
        ApiClient().services.editProduct(id, product).enqueue(object : Callback<ProductResponse?> {
            override fun onFailure(call: Call<ProductResponse?>, t: Throwable) {
                callback.productFailed()
            }

            override fun onResponse(
                call: Call<ProductResponse?>,
                response: Response<ProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.productSuccess(response.body())
                }else{
                    callback.productFailed()
                }
            }
        })
    }

    fun deleteProduct(id: String, callback: ProductRepositoryCallback<ProductResponse>) {
        ApiClient().services.deleteProduct(id).enqueue(object : Callback<ProductResponse?> {
            override fun onFailure(call: Call<ProductResponse?>, t: Throwable) {
                callback.productFailed()
            }

            override fun onResponse(
                call: Call<ProductResponse?>,
                response: Response<ProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.productSuccess(response.body())
                }else{
                    callback.productFailed()
                }
            }

        })
    }

    fun uploadPhotoProduct(id: String, photo: MultipartBody.Part, callback: UploadPhotoProductRepositoryCallback<ResponseBody>) {

        ApiClient().services.uploadPhotoProduct(id, photo).enqueue(object : Callback<ResponseBody?> {
            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                callback.uploadProductFailed()
            }

            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful){
                    callback.uploadProductSuccess(response.body())
                }else{
                    callback.uploadProductFailed()
                }
            }

        })
    }

    //CUSTOMER
    fun getAllCustomer(callback: CustomerRepositoryCallback<CustomerResponse>) {
        ApiClient().services.getAllCustomer().enqueue(object : Callback<CustomerResponse?> {
            override fun onFailure(call: Call<CustomerResponse?>, t: Throwable) {
                callback.customerFailed()
            }

            override fun onResponse(
                call: Call<CustomerResponse?>,
                response: Response<CustomerResponse?>
            ) {
                if (response.isSuccessful){
                    callback.customerSuccess(response.body())
                }else{
                    callback.customerFailed()
                }
            }
        })
    }

    fun getCustomerBySearch(query: String, callback: CustomerRepositoryCallback<CustomerResponse>){
        ApiClient().services.getCustomerBySearch(query).enqueue(object : Callback<CustomerResponse?> {
            override fun onFailure(call: Call<CustomerResponse?>, t: Throwable) {
                callback.customerFailed()
            }

            override fun onResponse(
                call: Call<CustomerResponse?>,
                response: Response<CustomerResponse?>
            ) {
                if (response.isSuccessful){
                    callback.customerSuccess(response.body())
                }else{
                    callback.customerFailed()
                }
            }
        })
    }

    fun addCustomer(customer: Customer, callback: CustomerRepositoryCallback<CustomerResponse>) {

        ApiClient().services.addCustomer(customer).enqueue(object : Callback<CustomerResponse?> {
            override fun onFailure(call: Call<CustomerResponse?>, t: Throwable) {
                callback.customerFailed()
            }

            override fun onResponse(
                call: Call<CustomerResponse?>,
                response: Response<CustomerResponse?>
            ) {
                if (response.isSuccessful){
                    callback.customerSuccess(response.body())
                }else{
                    callback.customerFailed()
                }
            }

        })
    }

    fun editCustomer(id: String, customer: Customer, callback: CustomerRepositoryCallback<CustomerResponse>) {

        ApiClient().services.editCustomer(id, customer).enqueue(object : Callback<CustomerResponse?> {
            override fun onFailure(call: Call<CustomerResponse?>, t: Throwable) {
                callback.customerFailed()
            }

            override fun onResponse(
                call: Call<CustomerResponse?>,
                response: Response<CustomerResponse?>
            ) {
                if (response.isSuccessful){
                    callback.customerSuccess(response.body())
                }else{
                    callback.customerFailed()
                }
            }

        })
    }

    fun deleteCustomer(id: String, last_emp: String, callback: CustomerRepositoryCallback<CustomerResponse>) {

        ApiClient().services.deleteCustomer(id, last_emp).enqueue(object : Callback<CustomerResponse?> {
            override fun onFailure(call: Call<CustomerResponse?>, t: Throwable) {
                callback.customerFailed()
            }

            override fun onResponse(
                call: Call<CustomerResponse?>,
                response: Response<CustomerResponse?>
            ) {
                if (response.isSuccessful){
                    callback.customerSuccess(response.body())
                }else{
                    callback.customerFailed()
                }
            }

        })
    }

//    PET SIZE
    fun getAllPetSize(callback: PetSizeRepositoryCallback<PetSizeResponse>) {

        ApiClient().services.getAllPetSize().enqueue(object : Callback<PetSizeResponse?> {
            override fun onFailure(call: Call<PetSizeResponse?>, t: Throwable) {
                callback.petSizeFailed()
            }

            override fun onResponse(
                call: Call<PetSizeResponse?>,
                response: Response<PetSizeResponse?>
            ) {
                if (response.isSuccessful){
                    callback.petSizeSuccess(response.body())
                }else{
                    callback.petSizeFailed()
                }
            }
        })
    }

    fun getPetSizeBySearch(query: String, callback: PetSizeRepositoryCallback<PetSizeResponse>) {
        ApiClient().services.getPetSizeBySearch(query).enqueue(object : Callback<PetSizeResponse?> {
            override fun onFailure(call: Call<PetSizeResponse?>, t: Throwable) {
                callback.petSizeFailed()
            }

            override fun onResponse(
                call: Call<PetSizeResponse?>,
                response: Response<PetSizeResponse?>
            ) {
                if (response.isSuccessful){
                    callback.petSizeSuccess(response.body())
                }else{
                    callback.petSizeFailed()
                }
            }
        })
    }

    fun addPetSize(petSize: PetSize, callback: PetSizeRepositoryCallback<PetSizeResponse>) {

        ApiClient().services.addPetSize(petSize).enqueue(object : Callback<PetSizeResponse?> {
            override fun onFailure(call: Call<PetSizeResponse?>, t: Throwable) {
                callback.petSizeFailed()
            }

            override fun onResponse(
                call: Call<PetSizeResponse?>,
                response: Response<PetSizeResponse?>
            ) {
                if (response.isSuccessful){
                    callback.petSizeSuccess(response.body())
                }else{
                    callback.petSizeFailed()
                }
            }
        })
    }

    fun editPetSize(id: String, petSize: PetSize, callback: PetSizeRepositoryCallback<PetSizeResponse>) {

        ApiClient().services.editPetSize(id, petSize).enqueue(object : Callback<PetSizeResponse?> {
            override fun onFailure(call: Call<PetSizeResponse?>, t: Throwable) {
                callback.petSizeFailed()
            }

            override fun onResponse(
                call: Call<PetSizeResponse?>,
                response: Response<PetSizeResponse?>
            ) {
                if (response.isSuccessful){
                    callback.petSizeSuccess(response.body())
                }else{
                    callback.petSizeFailed()
                }
            }
        })
    }

    fun deletePetSize(id: String, callback: PetSizeRepositoryCallback<PetSizeResponse>) {
        ApiClient().services.deletePetSize(id).enqueue(object : Callback<PetSizeResponse?> {
            override fun onFailure(call: Call<PetSizeResponse?>, t: Throwable) {
                callback.petSizeFailed()
            }

            override fun onResponse(
                call: Call<PetSizeResponse?>,
                response: Response<PetSizeResponse?>
            ) {
                if (response.isSuccessful){
                    callback.petSizeSuccess(response.body())
                }else{
                    callback.petSizeFailed()
                }
            }
        })
    }


//    PET TYPE
    fun getAllPetType(callback: PetTypeRepositoryCallback<PetTypeResponse>) {
        ApiClient().services.getAllPetType().enqueue(object : Callback<PetTypeResponse?> {
            override fun onFailure(call: Call<PetTypeResponse?>, t: Throwable) {
                callback.petTypeFailed()
            }

            override fun onResponse(
                call: Call<PetTypeResponse?>,
                response: Response<PetTypeResponse?>
            ) {
                if (response.isSuccessful){
                    callback.petTypeSuccess(response.body())
                }else{
                    callback.petTypeFailed()
                }
            }
        })
    }

    fun getPetTypeBySearch(query: String, callback: PetTypeRepositoryCallback<PetTypeResponse>){
        ApiClient().services.getPetTypeBySearch(query).enqueue(object : Callback<PetTypeResponse>{
            override fun onFailure(call: Call<PetTypeResponse?>, t: Throwable) {
                callback.petTypeFailed()
            }

            override fun onResponse(
                call: Call<PetTypeResponse?>,
                response: Response<PetTypeResponse?>
            ) {
                if (response.isSuccessful){
                    callback.petTypeSuccess(response.body())
                }else{
                    callback.petTypeFailed()
                }
            }
        })
    }

    fun addPetType(petType: PetType, callback: PetTypeRepositoryCallback<PetTypeResponse>) {

        ApiClient().services.addPetType(petType).enqueue(object : Callback<PetTypeResponse?> {
            override fun onFailure(call: Call<PetTypeResponse?>, t: Throwable) {
                callback.petTypeFailed()
            }

            override fun onResponse(
                call: Call<PetTypeResponse?>,
                response: Response<PetTypeResponse?>
            ) {
                if (response.isSuccessful){
                    callback.petTypeSuccess(response.body())
                }else{
                    callback.petTypeFailed()
                }
            }
        })
    }

    fun editPetType(id: String, petType: PetType, callback: PetTypeRepositoryCallback<PetTypeResponse>) {

        ApiClient().services.editPetType(id, petType).enqueue(object : Callback<PetTypeResponse?> {
            override fun onFailure(call: Call<PetTypeResponse?>, t: Throwable) {
                callback.petTypeFailed()
            }

            override fun onResponse(
                call: Call<PetTypeResponse?>,
                response: Response<PetTypeResponse?>
            ) {
                if (response.isSuccessful){
                    callback.petTypeSuccess(response.body())
                }else{
                    callback.petTypeFailed()
                }
            }
        })
    }

    fun deletePetType(id: String, callback: PetTypeRepositoryCallback<PetTypeResponse>) {

        ApiClient().services.deletePetType(id).enqueue(object : Callback<PetTypeResponse?> {
            override fun onFailure(call: Call<PetTypeResponse?>, t: Throwable) {
                callback.petTypeFailed()
            }

            override fun onResponse(
                call: Call<PetTypeResponse?>,
                response: Response<PetTypeResponse?>
            ) {
                if (response.isSuccessful){
                    callback.petTypeSuccess(response.body())
                }else{
                    callback.petTypeFailed()
                }
            }
        })
    }

    //SUPPLIER
    fun getAllSupplier(callback: SupplierRepositoryCallback<SupplierResponse>) {

        ApiClient().services.getAllSupplier().enqueue(object : Callback<SupplierResponse?> {
            override fun onFailure(call: Call<SupplierResponse?>, t: Throwable) {
                callback.supplierFailed()
            }

            override fun onResponse(
                call: Call<SupplierResponse?>,
                response: Response<SupplierResponse?>
            ) {
                if (response.isSuccessful){
                    callback.supplierSuccess(response.body())
                }else{
                    callback.supplierFailed()
                }
            }

        })
    }

    fun getSupplierBySearch(query: String, callback: SupplierRepositoryCallback<SupplierResponse>){
        ApiClient().services.getSupplierBySearch(query).enqueue(object : Callback<SupplierResponse?> {
            override fun onFailure(call: Call<SupplierResponse?>, t: Throwable) {
                callback.supplierFailed()
            }

            override fun onResponse(
                call: Call<SupplierResponse?>,
                response: Response<SupplierResponse?>
            ) {
                if (response.isSuccessful){
                    callback.supplierSuccess(response.body())
                }else{
                    callback.supplierFailed()
                }
            }
        })
    }

    fun addSupplier(supplier: Supplier, callback: SupplierRepositoryCallback<SupplierResponse>) {

        ApiClient().services.addSupplier(supplier).enqueue(object : Callback<SupplierResponse?> {
            override fun onFailure(call: Call<SupplierResponse?>, t: Throwable) {
                callback.supplierFailed()
            }

            override fun onResponse(
                call: Call<SupplierResponse?>,
                response: Response<SupplierResponse?>
            ) {
                if (response.isSuccessful){
                    callback.supplierSuccess(response.body())
                }else{
                    callback.supplierFailed()
                }
            }
        })
    }

    fun editSupplier(id: String, supplier: Supplier, callback: SupplierRepositoryCallback<SupplierResponse>) {

        ApiClient().services.editSupplier(id, supplier).enqueue(object : Callback<SupplierResponse?> {
            override fun onFailure(call: Call<SupplierResponse?>, t: Throwable) {
                callback.supplierFailed()
            }

            override fun onResponse(
                call: Call<SupplierResponse?>,
                response: Response<SupplierResponse?>
            ) {
                if (response.isSuccessful){
                    callback.supplierSuccess(response.body())
                }else{
                    callback.supplierFailed()
                }
            }
        })
    }

    fun deleteSupplier(id: String, callback: SupplierRepositoryCallback<SupplierResponse>) {

        ApiClient().services.deleteSupplier(id).enqueue(object : Callback<SupplierResponse?> {
            override fun onFailure(call: Call<SupplierResponse?>, t: Throwable) {
                callback.supplierFailed()
            }

            override fun onResponse(
                call: Call<SupplierResponse?>,
                response: Response<SupplierResponse?>
            ) {
                if (response.isSuccessful){
                    callback.supplierSuccess(response.body())
                }else{
                    callback.supplierFailed()
                }
            }
        })
    }

    //SERVICE
    fun getAllService(callback: ServiceRepositoryCallback<ServiceResponse>) {

        ApiClient().services.getAllService().enqueue(object : Callback<ServiceResponse?> {
            override fun onFailure(call: Call<ServiceResponse?>, t: Throwable) {
                callback.serviceFailed()
            }

            override fun onResponse(
                call: Call<ServiceResponse?>,
                response: Response<ServiceResponse?>
            ) {
                if (response.isSuccessful){
                    callback.serviceSuccess(response.body())
                }else{
                    callback.serviceFailed()
                }
            }
        })
    }

    fun getServiceBySearch(query: String, callback: ServiceRepositoryCallback<ServiceResponse>){
        ApiClient().services.getServiceBySearch(query).enqueue(object : Callback<ServiceResponse?>{
            override fun onFailure(call: Call<ServiceResponse?>, t: Throwable) {
                callback.serviceFailed()
            }

            override fun onResponse(
                call: Call<ServiceResponse?>,
                response: Response<ServiceResponse?>
            ) {
                if (response.isSuccessful){
                    callback.serviceSuccess(response.body())
                }else{
                    callback.serviceFailed()
                }
            }
        })
    }

    fun addService(service: Service, callback: ServiceRepositoryCallback<ServiceResponse>) {

        ApiClient().services.addService(service).enqueue(object : Callback<ServiceResponse?> {
            override fun onFailure(call: Call<ServiceResponse?>, t: Throwable) {
                callback.serviceFailed()
            }

            override fun onResponse(
                call: Call<ServiceResponse?>,
                response: Response<ServiceResponse?>
            ) {
                if (response.isSuccessful){
                    callback.serviceSuccess(response.body())
                }else{
                    callback.serviceFailed()
                }
            }
        })
    }

    fun editService(id: String, service: Service, callback: ServiceRepositoryCallback<ServiceResponse>) {
        ApiClient().services.editService(id, service).enqueue(object : Callback<ServiceResponse?> {
            override fun onFailure(call: Call<ServiceResponse?>, t: Throwable) {
                callback.serviceFailed()
            }

            override fun onResponse(
                call: Call<ServiceResponse?>,
                response: Response<ServiceResponse?>
            ) {
                if (response.isSuccessful){
                    callback.serviceSuccess(response.body())
                }else{
                    callback.serviceFailed()
                }
            }
        })
    }

    fun deleteService(id: String, callback : ServiceRepositoryCallback<ServiceResponse>){
        ApiClient().services.deleteService(id).enqueue(object : Callback<ServiceResponse?> {
            override fun onFailure(call: Call<ServiceResponse?>, t: Throwable) {
                callback.serviceFailed()
            }

            override fun onResponse(
                call: Call<ServiceResponse?>,
                response: Response<ServiceResponse?>
            ) {
                if (response.isSuccessful){
                    callback.serviceSuccess(response.body())
                }else{
                    callback.serviceFailed()
                }
            }
        })
    }

    //CUSTOMER PET
    fun getAllCustomerPet(callback: CustomerPetRepositoryCallback<CustomerPetResponse>){
        ApiClient().services.getAllCustomerPet().enqueue(object : Callback<CustomerPetResponse?> {
            override fun onFailure(call: Call<CustomerPetResponse?>, t: Throwable) {
                callback.customerPetFailed()
            }

            override fun onResponse(
                call: Call<CustomerPetResponse?>,
                response: Response<CustomerPetResponse?>
            ) {
                if (response.isSuccessful){
                    callback.customerPetSuccess(response.body())
                }else{
                    callback.customerPetFailed()
                }
            }
        })
    }

    fun getCustomerPetBySearch(query: String, callback: CustomerPetRepositoryCallback<CustomerPetResponse>){
        ApiClient().services.getCustomerPetBySearch(query).enqueue(object : Callback<CustomerPetResponse?>{
            override fun onFailure(call: Call<CustomerPetResponse?>, t: Throwable) {
                callback.customerPetFailed()
            }

            override fun onResponse(
                call: Call<CustomerPetResponse?>,
                response: Response<CustomerPetResponse?>
            ) {
                if (response.isSuccessful){
                    callback.customerPetSuccess(response.body())
                }else{
                    callback.customerPetFailed()
                }
            }
        })
    }

    fun addCustomerPet(customerPet: CustomerPet, callback: CustomerPetRepositoryCallback<CustomerPetResponse>){
        ApiClient().services.addCustomerPet(customerPet).enqueue(object : Callback<CustomerPetResponse?>{
            override fun onFailure(call: Call<CustomerPetResponse?>, t: Throwable) {
                callback.customerPetFailed()
            }

            override fun onResponse(
                call: Call<CustomerPetResponse?>,
                response: Response<CustomerPetResponse?>
            ) {
                if (response.isSuccessful){
                    callback.customerPetSuccess(response.body())
                }else{
                    callback.customerPetFailed()
                }
            }
        })
    }

    fun editCustomerPet(id: String, customerPet: CustomerPet, callback: CustomerPetRepositoryCallback<CustomerPetResponse>){
        ApiClient().services.editCustomerPet(id, customerPet).enqueue(object : Callback<CustomerPetResponse?>{
            override fun onFailure(call: Call<CustomerPetResponse?>, t: Throwable) {
                callback.customerPetFailed()
            }

            override fun onResponse(
                call: Call<CustomerPetResponse?>,
                response: Response<CustomerPetResponse?>
            ) {
                if (response.isSuccessful){
                    callback.customerPetSuccess(response.body())
                }else{
                    callback.customerPetFailed()
                }
            }
        })
    }

    fun deleteCustomerPet(id: String, callback: CustomerPetRepositoryCallback<CustomerPetResponse>){
        ApiClient().services.deleteCustomerPet(id).enqueue(object : Callback<CustomerPetResponse?> {
            override fun onFailure(call: Call<CustomerPetResponse?>, t: Throwable) {
                callback.customerPetFailed()
            }

            override fun onResponse(
                call: Call<CustomerPetResponse?>,
                response: Response<CustomerPetResponse?>
            ) {
                if (response.isSuccessful){
                    callback.customerPetSuccess(response.body())
                }else{
                    callback.customerPetFailed()
                }
            }
        })
    }

//    ORDER PRODUCT
    fun getAllOrderProduct(callback: OrderProductRepositoryCallback<OrderProductResponse>){
        ApiClient().services.getAllOrderProduct().enqueue(object : Callback<OrderProductResponse?>{
            override fun onFailure(call: Call<OrderProductResponse?>, t: Throwable) {
                callback.orderProductFailed()
            }

            override fun onResponse(
                call: Call<OrderProductResponse?>,
                response: Response<OrderProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.orderProductSuccess(response.body())
                }else{
                    callback.orderProductFailed()
                }
            }
        })
    }

    fun addOrderProduct(orderProduct: OrderProduct, callback: OrderProductRepositoryCallback<OrderProductResponse>){
        ApiClient().services.addOrderProduct(orderProduct).enqueue(object : Callback<OrderProductResponse?>{
            override fun onFailure(call: Call<OrderProductResponse?>, t: Throwable) {
                callback.orderProductFailed()
            }

            override fun onResponse(
                call: Call<OrderProductResponse?>,
                response: Response<OrderProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.orderProductSuccess(response.body())
                }else{
                    callback.orderProductFailed()
                }
            }
        })
    }

    fun editOrderProduct(id: String, orderProduct: OrderProduct, callback: OrderProductRepositoryCallback<OrderProductResponse>){
        ApiClient().services.editOrderProduct(id, orderProduct).enqueue(object : Callback<OrderProductResponse?>{
            override fun onFailure(call: Call<OrderProductResponse?>, t: Throwable) {
                callback.orderProductFailed()
            }

            override fun onResponse(
                call: Call<OrderProductResponse?>,
                response: Response<OrderProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.orderProductSuccess(response.body())
                }else{
                    callback.orderProductFailed()
                }
            }
        })
    }

    fun deleteOrderProduct(id: String, callback: OrderProductRepositoryCallback<OrderProductResponse>){
        ApiClient().services.deleteOrderProduct(id).enqueue(object : Callback<OrderProductResponse?>{
            override fun onFailure(call: Call<OrderProductResponse?>, t: Throwable) {
                callback.orderProductFailed()
            }

            override fun onResponse(
                call: Call<OrderProductResponse?>,
                response: Response<OrderProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.orderProductSuccess(response.body())
                }else{
                    callback.orderProductFailed()
                }
            }
        })
    }

    fun editTotalOrderProduct(id: String, callback: OrderProductRepositoryCallback<OrderProductResponse>){
        ApiClient().services.editTotalOrderProduct(id).enqueue(object : Callback<OrderProductResponse?>{
            override fun onFailure(call: Call<OrderProductResponse?>, t: Throwable) {
                callback.orderProductFailed()
            }

            override fun onResponse(
                call: Call<OrderProductResponse?>,
                response: Response<OrderProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.orderProductSuccess(response.body())
                }else{
                    callback.orderProductFailed()
                }
            }
        })
    }

    fun editDoneOrderProduct(id: String, callback: OrderProductRepositoryCallback<OrderProductResponse>){
        ApiClient().services.editDoneOrderProduct(id).enqueue(object : Callback<OrderProductResponse>{
            override fun onFailure(call: Call<OrderProductResponse?>, t: Throwable) {
                callback.orderProductFailed()
            }

            override fun onResponse(
                call: Call<OrderProductResponse?>,
                response: Response<OrderProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.orderProductSuccess(response.body())
                }else{
                    callback.orderProductFailed()
                }
            }
        })
    }

    fun editPrintOrderProduct(id: String, callback: OrderProductRepositoryCallback<OrderProductResponse>){
        ApiClient().services.editPrintOrderProduct(id).enqueue(object : Callback<OrderProductResponse?>{
            override fun onFailure(call: Call<OrderProductResponse?>, t: Throwable) {
                callback.orderProductFailed()
            }

            override fun onResponse(
                call: Call<OrderProductResponse?>,
                response: Response<OrderProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.orderProductSuccess(response.body())
                }else{
                    callback.orderProductFailed()
                }
            }
        })
    }

//    DETAIL ORDER PRODUCT
    fun getAllDetailOrderProduct(callback: DetailOrderProductRepositoryCallback<DetailOrderProductResponse>){
        ApiClient().services.getAllDetailOrderProduct().enqueue(object : Callback<DetailOrderProductResponse?>{
            override fun onFailure(call: Call<DetailOrderProductResponse?>, t: Throwable) {
                callback.detailOrderProductFailed()
            }
            override fun onResponse(
                call: Call<DetailOrderProductResponse?>,
                response: Response<DetailOrderProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailOrderProductSuccess(response.body())
                }else{
                    callback.detailOrderProductFailed()
                }
            }
        })
    }

    fun getDetailOrderProductByOrderId(id: String, callback: DetailOrderProductRepositoryCallback<DetailOrderProductResponse>){
        ApiClient().services.getDetailOrderProductByOrderId(id).enqueue(object : Callback<DetailOrderProductResponse?>{
            override fun onFailure(call: Call<DetailOrderProductResponse?>, t: Throwable) {
                callback.detailOrderProductFailed()
            }
            override fun onResponse(
                call: Call<DetailOrderProductResponse?>,
                response: Response<DetailOrderProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailOrderProductSuccess(response.body())
                }else{
                    callback.detailOrderProductFailed()
                }
            }
        })
    }

    fun addDetailOrderProduct(detailOrderProduct: DetailOrderProduct, callback: DetailOrderProductRepositoryCallback<DetailOrderProductResponse>){
        ApiClient().services.addDetailOrderProduct(detailOrderProduct).enqueue(object : Callback<DetailOrderProductResponse?>{
            override fun onFailure(call: Call<DetailOrderProductResponse?>, t: Throwable) {
                callback.detailOrderProductFailed()
            }
            override fun onResponse(
                call: Call<DetailOrderProductResponse?>,
                response: Response<DetailOrderProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailOrderProductSuccess(response.body())
                }else{
                    callback.detailOrderProductFailed()
                }
            }
        })
    }

    fun editDetailOrderProduct(detailOrderProduct: DetailOrderProduct, callback: DetailOrderProductRepositoryCallback<DetailOrderProductResponse>){
        ApiClient().services.editDetailOrderProduct(detailOrderProduct).enqueue(object : Callback<DetailOrderProductResponse?>{
            override fun onFailure(call: Call<DetailOrderProductResponse?>, t: Throwable) {
                callback.detailOrderProductFailed()
            }
            override fun onResponse(
                call: Call<DetailOrderProductResponse?>,
                response: Response<DetailOrderProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailOrderProductSuccess(response.body())
                }else{
                    callback.detailOrderProductFailed()
                }
            }
        })
    }

    fun deleteDetailOrderProduct(id_order: String, id_product: String, callback: DetailOrderProductRepositoryCallback<DetailOrderProductResponse>){
        ApiClient().services.deleteDetailOrderProduct(id_order, id_product).enqueue(object : Callback<DetailOrderProductResponse?>{
            override fun onFailure(call: Call<DetailOrderProductResponse?>, t: Throwable) {
                callback.detailOrderProductFailed()
            }
            override fun onResponse(
                call: Call<DetailOrderProductResponse?>,
                response: Response<DetailOrderProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailOrderProductSuccess(response.body())
                }else{
                    callback.detailOrderProductFailed()
                }
            }
        })
    }

//  TRANSACTION
    fun getAllTransaction(callback: TransactionRepositoryCallback<TransactionResponse>){
        ApiClient().services.getAllTransaction().enqueue(object : Callback<TransactionResponse?>{
            override fun onFailure(call: Call<TransactionResponse?>, t: Throwable) {
                callback.transactionFailed()
            }
            override fun onResponse(
                call: Call<TransactionResponse?>,
                response: Response<TransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.transactionSuccess(response.body())
                }else{
                    callback.transactionFailed()
                }
            }
        })
    }

    fun getAllProductTransaction(callback: TransactionRepositoryCallback<TransactionResponse>){
        ApiClient().services.getAllProductTransaction().enqueue(object : Callback<TransactionResponse?>{
            override fun onFailure(call: Call<TransactionResponse?>, t: Throwable) {
                callback.transactionFailed()
            }
            override fun onResponse(
                call: Call<TransactionResponse?>,
                response: Response<TransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.transactionSuccess(response.body())
                }else{
                    callback.transactionFailed()
                }
            }
        })
    }

    fun getAllServiceTransaction(callback: TransactionRepositoryCallback<TransactionResponse>){
        ApiClient().services.getAllServiceTransaction().enqueue(object : Callback<TransactionResponse?>{
            override fun onFailure(call: Call<TransactionResponse?>, t: Throwable) {
                callback.transactionFailed()
            }
            override fun onResponse(
                call: Call<TransactionResponse?>,
                response: Response<TransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.transactionSuccess(response.body())
                }else{
                    callback.transactionFailed()
                }
            }
        })
    }


    fun addTransaction(type: String, transaction: Transaction, callback: TransactionRepositoryCallback<TransactionResponse>){
        ApiClient().services.addTransaction(type, transaction).enqueue(object : Callback<TransactionResponse?>{
            override fun onFailure(call: Call<TransactionResponse?>, t: Throwable) {
                callback.transactionFailed()
            }
            override fun onResponse(
                call: Call<TransactionResponse?>,
                response: Response<TransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.transactionSuccess(response.body())
                }else{
                    callback.transactionFailed()
                }
            }
        })
    }

    fun editTotalTransaction(id: String, callback: TransactionRepositoryCallback<TransactionResponse>){
        ApiClient().services.editTotalTransaction(id).enqueue(object : Callback<TransactionResponse?>{
            override fun onFailure(call: Call<TransactionResponse?>, t: Throwable) {
                callback.transactionFailed()
            }
            override fun onResponse(
                call: Call<TransactionResponse?>,
                response: Response<TransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.transactionSuccess(response.body())
                }else{
                    callback.transactionFailed()
                }
            }
        })
    }

    fun editDoneTransaction(id: String, transaction: Transaction, callback: TransactionRepositoryCallback<TransactionResponse>){
        ApiClient().services.editDoneTransaction(id, transaction).enqueue(object : Callback<TransactionResponse?>{
            override fun onFailure(call: Call<TransactionResponse?>, t: Throwable) {
                callback.transactionFailed()
            }
            override fun onResponse(
                call: Call<TransactionResponse?>,
                response: Response<TransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.transactionSuccess(response.body())
                }else{
                    callback.transactionFailed()
                }
            }
        })
    }

    fun editStatusTransaction(id: String, transaction: Transaction, callback: TransactionRepositoryCallback<TransactionResponse>){
        ApiClient().services.editStatusTransaction(id, transaction).enqueue(object : Callback<TransactionResponse?>{
            override fun onFailure(call: Call<TransactionResponse?>, t: Throwable) {
                callback.transactionFailed()
            }
            override fun onResponse(
                call: Call<TransactionResponse?>,
                response: Response<TransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.transactionSuccess(response.body())
                }else{
                    callback.transactionFailed()
                }
            }
        })
    }

    fun cancelTransaction(id: String, callback: TransactionRepositoryCallback<TransactionResponse>){
        ApiClient().services.cancelTransaction(id).enqueue(object : Callback<TransactionResponse?>{
            override fun onFailure(call: Call<TransactionResponse?>, t: Throwable) {
                callback.transactionFailed()
            }
            override fun onResponse(
                call: Call<TransactionResponse?>,
                response: Response<TransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.transactionSuccess(response.body())
                }else{
                    callback.transactionFailed()
                }
            }
        })
    }

//  DETAIL PRODUCT TRANSACTION
    fun getAllDetailProductTransaction(callback: DetailProductTransactionRepositoryCallback<DetailProductTransactionResponse>){
        ApiClient().services.getAllDetailProductTransaction().enqueue(object : Callback<DetailProductTransactionResponse?>{
            override fun onFailure(call: Call<DetailProductTransactionResponse?>, t: Throwable) {
                callback.detailProductTransactionFailed()
            }
            override fun onResponse(
                call: Call<DetailProductTransactionResponse?>,
                response: Response<DetailProductTransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailProductTransactionSuccess(response.body())
                }else{
                    callback.detailProductTransactionFailed()
                }
            }
        })
    }

    fun addDetailProductTransaction(detailProductTransaction: DetailProductTransaction, callback: DetailProductTransactionRepositoryCallback<DetailProductTransactionResponse>){
        ApiClient().services.addDetailProductTransaction(detailProductTransaction).enqueue(object : Callback<DetailProductTransactionResponse?>{
            override fun onFailure(call: Call<DetailProductTransactionResponse?>, t: Throwable) {
                callback.detailProductTransactionFailed()
            }
            override fun onResponse(
                call: Call<DetailProductTransactionResponse?>,
                response: Response<DetailProductTransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailProductTransactionSuccess(response.body())
                }else{
                    callback.detailProductTransactionFailed()
                }
            }
        })
    }

    fun editDetailProductTransaction(detailProductTransaction: DetailProductTransaction, callback: DetailProductTransactionRepositoryCallback<DetailProductTransactionResponse>){
        ApiClient().services.editDetailProductTransaction(detailProductTransaction).enqueue(object : Callback<DetailProductTransactionResponse?>{
            override fun onFailure(call: Call<DetailProductTransactionResponse?>, t: Throwable) {
                callback.detailProductTransactionFailed()
            }
            override fun onResponse(
                call: Call<DetailProductTransactionResponse?>,
                response: Response<DetailProductTransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailProductTransactionSuccess(response.body())
                }else{
                    callback.detailProductTransactionFailed()
                }
            }
        })
    }

    fun deleteDetailProductTransaction(id_transaction: String, id_product: String, callback: DetailProductTransactionRepositoryCallback<DetailProductTransactionResponse>){
        ApiClient().services.deleteDetailProductTransaction(id_transaction, id_product).enqueue(object : Callback<DetailProductTransactionResponse?>{
            override fun onFailure(call: Call<DetailProductTransactionResponse?>, t: Throwable) {
                callback.detailProductTransactionFailed()
            }
            override fun onResponse(
                call: Call<DetailProductTransactionResponse?>,
                response: Response<DetailProductTransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailProductTransactionSuccess(response.body())
                }else{
                    callback.detailProductTransactionFailed()
                }
            }
        })
    }

    fun getDetailProductTransactionByIdTransaction(id_transaction: String, callback: DetailProductTransactionRepositoryCallback<DetailProductTransactionResponse>){
        ApiClient().services.getDetailProductTransactionByIdTransaction(id_transaction).enqueue(object : Callback<DetailProductTransactionResponse?>{
            override fun onFailure(call: Call<DetailProductTransactionResponse?>, t: Throwable) {
                callback.detailProductTransactionFailed()
            }
            override fun onResponse(
                call: Call<DetailProductTransactionResponse?>,
                response: Response<DetailProductTransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailProductTransactionSuccess(response.body())
                }else{
                    callback.detailProductTransactionFailed()
                }
            }
        })
    }

    fun deleteAllDetailProductTransaction(id_transaction: String, callback: DetailProductTransactionRepositoryCallback<DetailProductTransactionResponse>){
        ApiClient().services.deleteAllDetailProductTransaction(id_transaction).enqueue(object : Callback<DetailProductTransactionResponse?>{
            override fun onFailure(call: Call<DetailProductTransactionResponse?>, t: Throwable) {
                callback.detailProductTransactionFailed()
            }
            override fun onResponse(
                call: Call<DetailProductTransactionResponse?>,
                response: Response<DetailProductTransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailProductTransactionSuccess(response.body())
                }else{
                    callback.detailProductTransactionFailed()
                }
            }
        })
    }

//  DETAIL SERVICE TRANSACTION
    fun getAllDetailServiceTransaction(callback: DetailServiceTransactionRepositoryCallback<DetailServiceTransactionResponse>){
        ApiClient().services.getAllDetailServiceTransaction().enqueue(object : Callback<DetailServiceTransactionResponse?>{
            override fun onFailure(call: Call<DetailServiceTransactionResponse?>, t: Throwable) {
                callback.detailServiceTransactionFailed()
            }
            override fun onResponse(
                call: Call<DetailServiceTransactionResponse?>,
                response: Response<DetailServiceTransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailServiceTransactionSuccess(response.body())
                }else{
                    callback.detailServiceTransactionFailed()
                }
            }
        })
    }

    fun addDetailServiceTransaction(detailServiceTransaction: DetailServiceTransaction, callback: DetailServiceTransactionRepositoryCallback<DetailServiceTransactionResponse>){
        ApiClient().services.addDetailServiceTransaction(detailServiceTransaction).enqueue(object : Callback<DetailServiceTransactionResponse?>{
            override fun onFailure(call: Call<DetailServiceTransactionResponse?>, t: Throwable) {
                callback.detailServiceTransactionFailed()
            }
            override fun onResponse(
                call: Call<DetailServiceTransactionResponse?>,
                response: Response<DetailServiceTransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailServiceTransactionSuccess(response.body())
                }else{
                    callback.detailServiceTransactionFailed()
                }
            }
        })
    }

    fun editDetailServiceTransaction(detailServiceTransaction: DetailServiceTransaction, callback: DetailServiceTransactionRepositoryCallback<DetailServiceTransactionResponse>){
        ApiClient().services.editDetailServiceTransaction(detailServiceTransaction).enqueue(object : Callback<DetailServiceTransactionResponse?>{
            override fun onFailure(call: Call<DetailServiceTransactionResponse?>, t: Throwable) {
                callback.detailServiceTransactionFailed()
            }
            override fun onResponse(
                call: Call<DetailServiceTransactionResponse?>,
                response: Response<DetailServiceTransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailServiceTransactionSuccess(response.body())
                }else{
                    callback.detailServiceTransactionFailed()
                }
            }
        })
    }

    fun deleteDetailServiceTransaction(id_transaction: String, id_service: String, callback: DetailServiceTransactionRepositoryCallback<DetailServiceTransactionResponse>){
        ApiClient().services.deleteDetailServiceTransaction(id_transaction, id_service).enqueue(object : Callback<DetailServiceTransactionResponse?>{
            override fun onFailure(call: Call<DetailServiceTransactionResponse?>, t: Throwable) {
                callback.detailServiceTransactionFailed()
            }
            override fun onResponse(
                call: Call<DetailServiceTransactionResponse?>,
                response: Response<DetailServiceTransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailServiceTransactionSuccess(response.body())
                }else{
                    callback.detailServiceTransactionFailed()
                }
            }
        })
    }

    fun getDetailServiceTransactionByIdTransaction(id_transaction: String, callback: DetailServiceTransactionRepositoryCallback<DetailServiceTransactionResponse>){
        ApiClient().services.getDetailServiceTransactionByIdTransaction(id_transaction).enqueue(object : Callback<DetailServiceTransactionResponse?>{
            override fun onFailure(call: Call<DetailServiceTransactionResponse?>, t: Throwable) {
                callback.detailServiceTransactionFailed()
            }
            override fun onResponse(
                call: Call<DetailServiceTransactionResponse?>,
                response: Response<DetailServiceTransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailServiceTransactionSuccess(response.body())
                }else{
                    callback.detailServiceTransactionFailed()
                }
            }
        })
    }

    fun deleteAllDetailServiceTransaction(id_transaction: String, callback: DetailServiceTransactionRepositoryCallback<DetailServiceTransactionResponse>){
        ApiClient().services.deleteAllDetailServiceTransaction(id_transaction).enqueue(object : Callback<DetailServiceTransactionResponse?>{
            override fun onFailure(call: Call<DetailServiceTransactionResponse?>, t: Throwable) {
                callback.detailServiceTransactionFailed()
            }
            override fun onResponse(
                call: Call<DetailServiceTransactionResponse?>,
                response: Response<DetailServiceTransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailServiceTransactionSuccess(response.body())
                }else{
                    callback.detailServiceTransactionFailed()
                }
            }
        })
    }

    fun getMinProduct(callback: MinProductRepositoryCallback<ProductResponse>){
        ApiClient().services.getMinProduct().enqueue(object : Callback<ProductResponse?>{
            override fun onFailure(call: Call<ProductResponse?>, t: Throwable) {
                callback.minProductFailed()
            }
            override fun onResponse(
                call: Call<ProductResponse?>,
                response: Response<ProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.minProductSuccess(response.body())
                }else{
                    callback.minProductFailed()
                }
            }
        })
    }
}
