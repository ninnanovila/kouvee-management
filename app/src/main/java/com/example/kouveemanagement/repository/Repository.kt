package com.example.kouveemanagement.repository

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
                callback.loginFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                when {
                    response.isSuccessful -> {
                        callback.loginSuccess(response.body())
                    }
                    response.code() == 404 -> {
                        callback.loginFailed("User not exist..")
                    }
                    response.code() == 500 -> {
                        callback.loginFailed("Wrong password..")
                    }
                    else -> callback.loginFailed("Else ...")
                }
            }
        })
    }

//    EMPLOYEE
    fun getAllEmployee(callback: EmployeeRepositoryCallback<EmployeeResponse>) {
        ApiClient().services.getAllEmployee().enqueue(object : Callback<EmployeeResponse?> {
            override fun onFailure(call: Call<EmployeeResponse?>, t: Throwable) {
                callback.employeeFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<EmployeeResponse?>,
                response: Response<EmployeeResponse?>
            ) {
                if (response.isSuccessful){
                    callback.employeeSuccess(response.body())
                }else{
                    callback.employeeFailed("Show error..")
                }
            }

        })
    }

    fun getEmployeeBySearch(query: String, callback: EmployeeRepositoryCallback<EmployeeResponse>){
        ApiClient().services.getEmployeeBySearch(query).enqueue(object : Callback<EmployeeResponse?> {
            override fun onFailure(call: Call<EmployeeResponse?>, t: Throwable) {
                callback.employeeFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<EmployeeResponse?>,
                response: Response<EmployeeResponse?>
            ) {
                if (response.isSuccessful){
                    callback.employeeSuccess(response.body())
                }else{
                    callback.employeeFailed("Show error..")
                }
            }
        })
    }

    fun addEmployee(employee: Employee, callback: EmployeeRepositoryCallback<EmployeeResponse>) {

        ApiClient().services.addEmployee(employee).enqueue(object : Callback<EmployeeResponse?> {
            override fun onFailure(call: Call<EmployeeResponse?>, t: Throwable) {
                callback.employeeFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<EmployeeResponse?>,
                response: Response<EmployeeResponse?>
            ) {
                if (response.isSuccessful){
                    callback.employeeSuccess(response.body())
                }else if (response.code() == 500){
                    callback.employeeFailed("Create error..")
                }
            }
        })
    }

    fun editEmployee(id: String, employee: Employee, callback: EmployeeRepositoryCallback<EmployeeResponse>) {

        ApiClient().services.editEmployee(id, employee).enqueue(object : Callback<EmployeeResponse?> {
            override fun onFailure(call: Call<EmployeeResponse?>, t: Throwable) {
                callback.employeeFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<EmployeeResponse?>,
                response: Response<EmployeeResponse?>
            ) {
                if (response.isSuccessful){
                    callback.employeeSuccess(response.body())
                }else if (response.code() == 500){
                    callback.employeeFailed("Edit error..")
                }
            }
        })
    }

    fun deleteEmployee(id: String, callback: EmployeeRepositoryCallback<EmployeeResponse>) {

        ApiClient().services.deleteEmployee(id).enqueue(object : Callback<EmployeeResponse?> {
            override fun onFailure(call: Call<EmployeeResponse?>, t: Throwable) {
                callback.employeeFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<EmployeeResponse?>,
                response: Response<EmployeeResponse?>
            ) {
                if (response.isSuccessful){
                    callback.employeeSuccess(response.body())
                }else if (response.code() == 404){
                    callback.employeeFailed("Employee not found..")
                }
            }
        })
    }

//    CUSTOMER
    fun getAllCustomer(callback: CustomerRepositoryCallback<CustomerResponse>) {
        ApiClient().services.getAllCustomer().enqueue(object : Callback<CustomerResponse?> {
            override fun onFailure(call: Call<CustomerResponse?>, t: Throwable) {
                callback.customerFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<CustomerResponse?>,
                response: Response<CustomerResponse?>
            ) {
                if (response.isSuccessful){
                    callback.customerSuccess(response.body())
                }else if (response.code() == 500){
                    callback.customerFailed("Show error..")
                }
            }
        })
    }

    fun getCustomerBySearch(query: String, callback: CustomerRepositoryCallback<CustomerResponse>){
        ApiClient().services.getCustomerBySearch(query).enqueue(object : Callback<CustomerResponse?> {
            override fun onFailure(call: Call<CustomerResponse?>, t: Throwable) {
                callback.customerFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<CustomerResponse?>,
                response: Response<CustomerResponse?>
            ) {
                if (response.isSuccessful){
                    callback.customerSuccess(response.body())
                }else if (response.code() == 500){
                    callback.customerFailed("Show error..")
                }
            }
        })
    }

    fun addCustomer(customer: Customer, callback: CustomerRepositoryCallback<CustomerResponse>) {

        ApiClient().services.addCustomer(customer).enqueue(object : Callback<CustomerResponse?> {
            override fun onFailure(call: Call<CustomerResponse?>, t: Throwable) {
                callback.customerFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<CustomerResponse?>,
                response: Response<CustomerResponse?>
            ) {
                if (response.isSuccessful){
                    callback.customerSuccess(response.body())
                }else if (response.code() == 500){
                    callback.customerFailed("Create error..")
                }
            }

        })
    }

    fun editCustomer(id: String, customer: Customer, callback: CustomerRepositoryCallback<CustomerResponse>) {

        ApiClient().services.editCustomer(id, customer).enqueue(object : Callback<CustomerResponse?> {
            override fun onFailure(call: Call<CustomerResponse?>, t: Throwable) {
                callback.customerFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<CustomerResponse?>,
                response: Response<CustomerResponse?>
            ) {
                when {
                    response.isSuccessful -> {
                        callback.customerSuccess(response.body())
                    }
                    response.code() == 500 -> {
                        callback.customerFailed("Edit error..")
                    }
                    response.code() == 404 -> {
                        callback.customerFailed("Data not found..")
                    }
                    else -> callback.customerFailed("Else ...")
                }
            }
        })
    }

    fun deleteCustomer(id: String, last_emp: String, callback: CustomerRepositoryCallback<CustomerResponse>) {

        ApiClient().services.deleteCustomer(id, last_emp).enqueue(object : Callback<CustomerResponse?> {
            override fun onFailure(call: Call<CustomerResponse?>, t: Throwable) {
                callback.customerFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<CustomerResponse?>,
                response: Response<CustomerResponse?>
            ) {
                when {
                    response.isSuccessful -> {
                        callback.customerSuccess(response.body())
                    }
                    response.code() == 500 -> {
                        callback.customerFailed("Delete error..")
                    }
                    response.code() == 404 -> {
                        callback.customerFailed("Data not found..")
                    }
                    else -> callback.customerFailed("Else ...")
                }
            }
        })
    }

//    SERVICE
    fun getAllService(callback: ServiceRepositoryCallback<ServiceResponse>) {

        ApiClient().services.getAllService().enqueue(object : Callback<ServiceResponse?> {
            override fun onFailure(call: Call<ServiceResponse?>, t: Throwable) {
                callback.serviceFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<ServiceResponse?>,
                response: Response<ServiceResponse?>
            ) {
                if (response.isSuccessful){
                    callback.serviceSuccess(response.body())
                }else{
                    callback.serviceFailed("Show error..")
                }
            }
        })
    }

    fun getServiceBySearch(query: String, callback: ServiceRepositoryCallback<ServiceResponse>){
        ApiClient().services.getServiceBySearch(query).enqueue(object : Callback<ServiceResponse?>{
            override fun onFailure(call: Call<ServiceResponse?>, t: Throwable) {
                callback.serviceFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<ServiceResponse?>,
                response: Response<ServiceResponse?>
            ) {
                if (response.isSuccessful){
                    callback.serviceSuccess(response.body())
                }else{
                    callback.serviceFailed("Show error..")
                }
            }
        })
    }

    fun addService(service: Service, callback: ServiceRepositoryCallback<ServiceResponse>) {

        ApiClient().services.addService(service).enqueue(object : Callback<ServiceResponse?> {
            override fun onFailure(call: Call<ServiceResponse?>, t: Throwable) {
                callback.serviceFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<ServiceResponse?>,
                response: Response<ServiceResponse?>
            ) {
                if (response.isSuccessful){
                    callback.serviceSuccess(response.body())
                }else if (response.code() == 500){
                    callback.serviceFailed("Create error..")
                }
            }
        })
    }

    fun editService(id: String, service: Service, callback: ServiceRepositoryCallback<ServiceResponse>) {
        ApiClient().services.editService(id, service).enqueue(object : Callback<ServiceResponse?> {
            override fun onFailure(call: Call<ServiceResponse?>, t: Throwable) {
                callback.serviceFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<ServiceResponse?>,
                response: Response<ServiceResponse?>
            ) {
                if (response.isSuccessful){
                    callback.serviceSuccess(response.body())
                }else if (response.code() == 500){
                    callback.serviceFailed("Edit error..")
                }
            }
        })
    }

    fun deleteService(id: String, callback : ServiceRepositoryCallback<ServiceResponse>){
        ApiClient().services.deleteService(id).enqueue(object : Callback<ServiceResponse?> {
            override fun onFailure(call: Call<ServiceResponse?>, t: Throwable) {
                callback.serviceFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<ServiceResponse?>,
                response: Response<ServiceResponse?>
            ) {
                if (response.isSuccessful){
                    callback.serviceSuccess(response.body())
                }else if (response.code() == 404){
                    callback.serviceFailed("Data not found..")
                }
            }
        })
    }

//    SUPPLIER
    fun getAllSupplier(callback: SupplierRepositoryCallback<SupplierResponse>) {

        ApiClient().services.getAllSupplier().enqueue(object : Callback<SupplierResponse?> {
            override fun onFailure(call: Call<SupplierResponse?>, t: Throwable) {
                callback.supplierFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<SupplierResponse?>,
                response: Response<SupplierResponse?>
            ) {
                if (response.isSuccessful){
                    callback.supplierSuccess(response.body())
                }else if (response.code() == 500){
                    callback.supplierFailed("Show error..")
                }
            }

        })
    }

    fun getSupplierBySearch(query: String, callback: SupplierRepositoryCallback<SupplierResponse>){
        ApiClient().services.getSupplierBySearch(query).enqueue(object : Callback<SupplierResponse?> {
            override fun onFailure(call: Call<SupplierResponse?>, t: Throwable) {
                callback.supplierFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<SupplierResponse?>,
                response: Response<SupplierResponse?>
            ) {
                if (response.isSuccessful){
                    callback.supplierSuccess(response.body())
                }else if (response.code() == 500){
                    callback.supplierFailed("Show error..")
                }
            }
        })
    }

    fun addSupplier(supplier: Supplier, callback: SupplierRepositoryCallback<SupplierResponse>) {
        ApiClient().services.addSupplier(supplier).enqueue(object : Callback<SupplierResponse?> {
            override fun onFailure(call: Call<SupplierResponse?>, t: Throwable) {
                callback.supplierFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<SupplierResponse?>,
                response: Response<SupplierResponse?>
            ) {
                when {
                    response.isSuccessful -> {
                        callback.supplierSuccess(response.body())
                    }
                    response.code() == 406 -> {
                        callback.supplierFailed("Supplier exist..")
                    }
                    response.code() == 500 -> {
                        callback.supplierFailed("Error add..")
                    }
                    else -> callback.supplierFailed("Else..")
                }
            }
        })
    }

    fun editSupplier(id: String, supplier: Supplier, callback: SupplierRepositoryCallback<SupplierResponse>) {
        ApiClient().services.editSupplier(id, supplier).enqueue(object : Callback<SupplierResponse?> {
            override fun onFailure(call: Call<SupplierResponse?>, t: Throwable) {
                callback.supplierFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<SupplierResponse?>,
                response: Response<SupplierResponse?>
            ) {
                when {
                    response.isSuccessful -> {
                        callback.supplierSuccess(response.body())
                    }
                    response.code() == 500 -> {
                        callback.supplierFailed("Edit error..")
                    }
                    response.code() == 404 -> {
                        callback.supplierFailed("Data not found..")
                    }
                    response.code() == 406 -> {
                        callback.supplierFailed("Supplier exist..")
                    }
                    else -> callback.supplierFailed("Else..")
                }
            }
        })
    }

    fun deleteSupplier(id: String, callback: SupplierRepositoryCallback<SupplierResponse>) {
        ApiClient().services.deleteSupplier(id).enqueue(object : Callback<SupplierResponse?> {
            override fun onFailure(call: Call<SupplierResponse?>, t: Throwable) {
                callback.supplierFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<SupplierResponse?>,
                response: Response<SupplierResponse?>
            ) {
                when {
                    response.isSuccessful -> {
                        callback.supplierSuccess(response.body())
                    }
                    response.code() == 406 -> {
                        callback.supplierFailed("Check constraint..")
                    }
                    response.code() == 404 -> {
                        callback.supplierFailed("Data not found..")
                    }
                    response.code() == 500 -> {
                        callback.supplierFailed("Delete error..")
                    }
                    else -> callback.supplierFailed("Else..")
                }
            }
        })
    }

//    PRODUCT
    fun getAllProduct(callback: ProductRepositoryCallback<ProductResponse>) {
        ApiClient().services.getAllProduct().enqueue(object : Callback<ProductResponse?> {
            override fun onFailure(call: Call<ProductResponse?>, t: Throwable) {
                callback.productFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<ProductResponse?>,
                response: Response<ProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.productSuccess(response.body())
                }else if (response.code() == 500){
                    callback.productFailed("Show error..")
                }
            }
        })
    }

    fun getProductBySearch(query: String, callback: ProductRepositoryCallback<ProductResponse>){
        ApiClient().services.getProductBySearch(query).enqueue(object : Callback<ProductResponse?>{
            override fun onFailure(call: Call<ProductResponse?>, t: Throwable) {
                callback.productFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<ProductResponse?>,
                response: Response<ProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.productSuccess(response.body())
                }else if (response.code() == 500){
                    callback.productFailed("Show error..")
                }
            }
        })
    }

    fun addProduct(product: Product, callback: ProductRepositoryCallback<ProductResponse>) {
        ApiClient().services.addProduct(product).enqueue(object : Callback<ProductResponse?> {
            override fun onFailure(call: Call<ProductResponse?>, t: Throwable) {
                callback.productFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<ProductResponse?>,
                response: Response<ProductResponse?>
            ) {
                when {
                    response.isSuccessful -> {
                        callback.productSuccess(response.body())
                    }
                    response.code() == 406 -> {
                        callback.productFailed("Product exist..")
                    }
                    response.code() == 500 -> {
                        callback.productFailed("Add error..")
                    }
                    else -> callback.productFailed("Else..")
                }
            }
        })
    }

    fun editProduct(id: String, product: Product, callback: ProductRepositoryCallback<ProductResponse>) {
        ApiClient().services.editProduct(id, product).enqueue(object : Callback<ProductResponse?> {
            override fun onFailure(call: Call<ProductResponse?>, t: Throwable) {
                callback.productFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<ProductResponse?>,
                response: Response<ProductResponse?>
            ) {
                when {
                    response.isSuccessful -> {
                        callback.productSuccess(response.body())
                    }
                    response.code() == 500 -> {
                        callback.productFailed("Edit error..")
                    }
                    response.code() == 404 -> {
                        callback.productFailed("Data not found..")
                    }
                    response.code() == 406 -> {
                        callback.productFailed("Product exist..")
                    }
                    else -> callback.productFailed("Else..")
                }
            }
        })
    }

    fun deleteProduct(id: String, callback: ProductRepositoryCallback<ProductResponse>) {
        ApiClient().services.deleteProduct(id).enqueue(object : Callback<ProductResponse?> {
            override fun onFailure(call: Call<ProductResponse?>, t: Throwable) {
                callback.productFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<ProductResponse?>,
                response: Response<ProductResponse?>
            ) {
                when {
                    response.isSuccessful -> {
                        callback.productSuccess(response.body())
                    }
                    response.code() == 404 -> {
                        callback.productFailed("Data not found..")
                    }
                    response.code() == 406 -> {
                        callback.productFailed("Check constraint..")
                    }
                    response.code() == 500 -> {
                        callback.productFailed("Delete error..")
                    }
                    else -> callback.productFailed("Else..")
                }
            }
        })
    }

    fun uploadPhotoProduct(id: String, photo: MultipartBody.Part, callback: UploadPhotoProductRepositoryCallback<ResponseBody>) {

        ApiClient().services.uploadPhotoProduct(id, photo).enqueue(object : Callback<ResponseBody?> {
            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                callback.uploadProductFailed(t.message.toString())
            }
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                when {
                    response.isSuccessful -> {
                        callback.uploadProductSuccess(response.body())
                    }
                    response.code() == 404 -> {
                        callback.uploadProductFailed("Data not found..")
                    }
                    response.code() == 500 -> {
                        callback.uploadProductFailed("Upload error..")
                    }
                    else -> callback.uploadProductFailed("Else...")
                }
            }

        })
    }

//    PET SIZE
    fun getAllPetSize(callback: PetSizeRepositoryCallback<PetSizeResponse>) {

        ApiClient().services.getAllPetSize().enqueue(object : Callback<PetSizeResponse?> {
            override fun onFailure(call: Call<PetSizeResponse?>, t: Throwable) {
                callback.petSizeFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<PetSizeResponse?>,
                response: Response<PetSizeResponse?>
            ) {
                if (response.isSuccessful){
                    callback.petSizeSuccess(response.body())
                }else if (response.code() == 500){
                    callback.petSizeFailed("Show error..")
                }
            }
        })
    }

    fun getPetSizeBySearch(query: String, callback: PetSizeRepositoryCallback<PetSizeResponse>) {
        ApiClient().services.getPetSizeBySearch(query).enqueue(object : Callback<PetSizeResponse?> {
            override fun onFailure(call: Call<PetSizeResponse?>, t: Throwable) {
                callback.petSizeFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<PetSizeResponse?>,
                response: Response<PetSizeResponse?>
            ) {
                if (response.isSuccessful){
                    callback.petSizeSuccess(response.body())
                }else if (response.code() == 500){
                    callback.petSizeFailed("Show error..")
                }
            }
        })
    }

    fun addPetSize(petSize: PetSize, callback: PetSizeRepositoryCallback<PetSizeResponse>) {

        ApiClient().services.addPetSize(petSize).enqueue(object : Callback<PetSizeResponse?> {
            override fun onFailure(call: Call<PetSizeResponse?>, t: Throwable) {
                callback.petSizeFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<PetSizeResponse?>,
                response: Response<PetSizeResponse?>
            ) {
                when {
                    response.isSuccessful -> {
                        callback.petSizeSuccess(response.body())
                    }
                    response.code() == 500 -> {
                        callback.petSizeFailed("Add error..")
                    }
                    response.code() == 406 -> {
                        callback.petSizeFailed("Pet size exist..")
                    }
                    else -> callback.petSizeFailed("Else..")
                }
            }
        })
    }

    fun editPetSize(id: String, petSize: PetSize, callback: PetSizeRepositoryCallback<PetSizeResponse>) {

        ApiClient().services.editPetSize(id, petSize).enqueue(object : Callback<PetSizeResponse?> {
            override fun onFailure(call: Call<PetSizeResponse?>, t: Throwable) {
                callback.petSizeFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<PetSizeResponse?>,
                response: Response<PetSizeResponse?>
            ) {
                when {
                    response.isSuccessful -> {
                        callback.petSizeSuccess(response.body())
                    }
                    response.code() == 500 -> {
                        callback.petSizeFailed("Edit error..")
                    }
                    response.code() == 404 -> {
                        callback.petSizeFailed("Data not found..")
                    }
                    response.code() == 406 -> {
                        callback.petSizeFailed("Pet size exist..")
                    }
                    else -> {
                        callback.petSizeFailed("Else..")
                    }
                }
            }
        })
    }

    fun deletePetSize(id: String, callback: PetSizeRepositoryCallback<PetSizeResponse>) {
        ApiClient().services.deletePetSize(id).enqueue(object : Callback<PetSizeResponse?> {
            override fun onFailure(call: Call<PetSizeResponse?>, t: Throwable) {
                callback.petSizeFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<PetSizeResponse?>,
                response: Response<PetSizeResponse?>
            ) {
                when {
                    response.isSuccessful -> {
                        callback.petSizeSuccess(response.body())
                    }
                    response.code() == 404 -> {
                        callback.petSizeFailed("Data not found..")
                    }
                    response.code() == 406 -> {
                        callback.petSizeFailed("Check constraint..")
                    }
                    response.code() == 500 -> {
                        callback.petSizeFailed("Delete error..")
                    }
                    else -> callback.petSizeFailed("Else..")
                }
            }
        })
    }

//    PET TYPE
    fun getAllPetType(callback: PetTypeRepositoryCallback<PetTypeResponse>) {
        ApiClient().services.getAllPetType().enqueue(object : Callback<PetTypeResponse?> {
            override fun onFailure(call: Call<PetTypeResponse?>, t: Throwable) {
                callback.petTypeFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<PetTypeResponse?>,
                response: Response<PetTypeResponse?>
            ) {
                if (response.isSuccessful){
                    callback.petTypeSuccess(response.body())
                }else if (response.code() == 500){
                    callback.petTypeFailed("Show error..")
                }
            }
        })
    }

    fun getPetTypeBySearch(query: String, callback: PetTypeRepositoryCallback<PetTypeResponse>){
        ApiClient().services.getPetTypeBySearch(query).enqueue(object : Callback<PetTypeResponse>{
            override fun onFailure(call: Call<PetTypeResponse?>, t: Throwable) {
                callback.petTypeFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<PetTypeResponse?>,
                response: Response<PetTypeResponse?>
            ) {
                if (response.isSuccessful){
                    callback.petTypeSuccess(response.body())
                }else if (response.code() == 500){
                    callback.petTypeFailed("Show error..")
                }
            }
        })
    }

    fun addPetType(petType: PetType, callback: PetTypeRepositoryCallback<PetTypeResponse>) {

        ApiClient().services.addPetType(petType).enqueue(object : Callback<PetTypeResponse?> {
            override fun onFailure(call: Call<PetTypeResponse?>, t: Throwable) {
                callback.petTypeFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<PetTypeResponse?>,
                response: Response<PetTypeResponse?>
            ) {
                when {
                    response.isSuccessful -> {
                        callback.petTypeSuccess(response.body())
                    }
                    response.code() == 500 -> {
                        callback.petTypeFailed("Add error..")
                    }
                    response.code() == 406 -> {
                        callback.petTypeFailed("Pet type exist..")
                    }
                    else -> callback.petTypeFailed("Else..")
                }
            }
        })
    }

    fun editPetType(id: String, petType: PetType, callback: PetTypeRepositoryCallback<PetTypeResponse>) {

        ApiClient().services.editPetType(id, petType).enqueue(object : Callback<PetTypeResponse?> {
            override fun onFailure(call: Call<PetTypeResponse?>, t: Throwable) {
                callback.petTypeFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<PetTypeResponse?>,
                response: Response<PetTypeResponse?>
            ) {
                when {
                    response.isSuccessful -> {
                        callback.petTypeSuccess(response.body())
                    }
                    response.code() == 500 -> {
                        callback.petTypeFailed("Edit error..")
                    }
                    response.code() == 404 -> {
                        callback.petTypeFailed("Data not found..")
                    }
                    response.code() == 406 -> {
                        callback.petTypeFailed("Pet type exist..")
                    }
                    else -> callback.petTypeFailed("Else..")
                }
            }
        })
    }

    fun deletePetType(id: String, callback: PetTypeRepositoryCallback<PetTypeResponse>) {

        ApiClient().services.deletePetType(id).enqueue(object : Callback<PetTypeResponse?> {
            override fun onFailure(call: Call<PetTypeResponse?>, t: Throwable) {
                callback.petTypeFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<PetTypeResponse?>,
                response: Response<PetTypeResponse?>
            ) {
                when {
                    response.isSuccessful -> {
                        callback.petTypeSuccess(response.body())
                    }
                    response.code() == 500 -> {
                        callback.petTypeFailed("Delete error..")
                    }
                    response.code() == 403 -> {
                        callback.petTypeFailed("Check constraint..")
                    }
                    response.code() == 404 -> {
                        callback.petTypeFailed("Data not found..")
                    }
                    else -> callback.petTypeFailed("Else..")
                }
            }
        })
    }

//    ORDER PRODUCT
    fun getAllOrderProduct(callback: OrderProductRepositoryCallback<OrderProductResponse>){
        ApiClient().services.getAllOrderProduct().enqueue(object : Callback<OrderProductResponse?>{
            override fun onFailure(call: Call<OrderProductResponse?>, t: Throwable) {
                callback.orderProductFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<OrderProductResponse?>,
                response: Response<OrderProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.orderProductSuccess(response.body())
                }else if (response.code() == 500){
                    callback.orderProductFailed("Show error..")
                }
            }
        })
    }

    fun addOrderProduct(orderProduct: OrderProduct, callback: OrderProductRepositoryCallback<OrderProductResponse>){
        ApiClient().services.addOrderProduct(orderProduct).enqueue(object : Callback<OrderProductResponse?>{
            override fun onFailure(call: Call<OrderProductResponse?>, t: Throwable) {
                callback.orderProductFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<OrderProductResponse?>,
                response: Response<OrderProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.orderProductSuccess(response.body())
                }else if (response.code() == 500){
                    callback.orderProductFailed("Add error..")
                }
            }
        })
    }

    fun editOrderProduct(id: String, orderProduct: OrderProduct, callback: OrderProductRepositoryCallback<OrderProductResponse>){
        ApiClient().services.editOrderProduct(id, orderProduct).enqueue(object : Callback<OrderProductResponse?>{
            override fun onFailure(call: Call<OrderProductResponse?>, t: Throwable) {
                callback.orderProductFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<OrderProductResponse?>,
                response: Response<OrderProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.orderProductSuccess(response.body())
                }else if (response.code() == 500){
                    callback.orderProductFailed("Edit error..")
                }
            }
        })
    }

    fun deleteOrderProduct(id: String, callback: OrderProductRepositoryCallback<OrderProductResponse>){
        ApiClient().services.deleteOrderProduct(id).enqueue(object : Callback<OrderProductResponse?>{
            override fun onFailure(call: Call<OrderProductResponse?>, t: Throwable) {
                callback.orderProductFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<OrderProductResponse?>,
                response: Response<OrderProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.orderProductSuccess(response.body())
                }else if (response.code() == 500){
                    callback.orderProductFailed("Delete error..")
                }
            }
        })
    }

    fun editTotalOrderProduct(id: String, callback: OrderProductRepositoryCallback<OrderProductResponse>){
        ApiClient().services.editTotalOrderProduct(id).enqueue(object : Callback<OrderProductResponse?>{
            override fun onFailure(call: Call<OrderProductResponse?>, t: Throwable) {
                callback.orderProductFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<OrderProductResponse?>,
                response: Response<OrderProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.orderProductSuccess(response.body())
                }else if (response.code() == 500){
                    callback.orderProductFailed("Total error..")
                }
            }
        })
    }

    fun editDoneOrderProduct(id: String, callback: OrderProductRepositoryCallback<OrderProductResponse>){
        ApiClient().services.editDoneOrderProduct(id).enqueue(object : Callback<OrderProductResponse>{
            override fun onFailure(call: Call<OrderProductResponse?>, t: Throwable) {
                callback.orderProductFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<OrderProductResponse?>,
                response: Response<OrderProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.orderProductSuccess(response.body())
                }else if (response.code() == 500){
                    callback.orderProductFailed("Done error..")
                }
            }
        })
    }

    fun editPrintOrderProduct(id: String, callback: OrderProductRepositoryCallback<OrderProductResponse>){
        ApiClient().services.editPrintOrderProduct(id).enqueue(object : Callback<OrderProductResponse?>{
            override fun onFailure(call: Call<OrderProductResponse?>, t: Throwable) {
                callback.orderProductFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<OrderProductResponse?>,
                response: Response<OrderProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.orderProductSuccess(response.body())
                }else if (response.code() == 500){
                    callback.orderProductFailed("Print error..")
                }
            }
        })
    }

//    DETAIL ORDER PRODUCT
    fun getAllDetailOrderProduct(callback: DetailOrderProductRepositoryCallback<DetailOrderProductResponse>){
        ApiClient().services.getAllDetailOrderProduct().enqueue(object : Callback<DetailOrderProductResponse?>{
            override fun onFailure(call: Call<DetailOrderProductResponse?>, t: Throwable) {
                callback.detailOrderProductFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<DetailOrderProductResponse?>,
                response: Response<DetailOrderProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailOrderProductSuccess(response.body())
                }else if (response.code() == 500){
                    callback.detailOrderProductFailed("Show error..")
                }
            }
        })
    }

    fun getDetailOrderProductByOrderId(id: String, callback: DetailOrderProductRepositoryCallback<DetailOrderProductResponse>){
        ApiClient().services.getDetailOrderProductByOrderId(id).enqueue(object : Callback<DetailOrderProductResponse?>{
            override fun onFailure(call: Call<DetailOrderProductResponse?>, t: Throwable) {
                callback.detailOrderProductFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<DetailOrderProductResponse?>,
                response: Response<DetailOrderProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailOrderProductSuccess(response.body())
                }else if (response.code() == 500){
                    callback.detailOrderProductFailed("Show error..")
                }
            }
        })
    }

    fun addDetailOrderProduct(detailOrderProduct: DetailOrderProduct, callback: DetailOrderProductRepositoryCallback<DetailOrderProductResponse>){
        ApiClient().services.addDetailOrderProduct(detailOrderProduct).enqueue(object : Callback<DetailOrderProductResponse?>{
            override fun onFailure(call: Call<DetailOrderProductResponse?>, t: Throwable) {
                callback.detailOrderProductFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<DetailOrderProductResponse?>,
                response: Response<DetailOrderProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailOrderProductSuccess(response.body())
                }else if (response.code() == 500){
                    callback.detailOrderProductFailed("Add error..")
                }
            }
        })
    }

    fun editDetailOrderProduct(detailOrderProduct: DetailOrderProduct, callback: DetailOrderProductRepositoryCallback<DetailOrderProductResponse>){
        ApiClient().services.editDetailOrderProduct(detailOrderProduct).enqueue(object : Callback<DetailOrderProductResponse?>{
            override fun onFailure(call: Call<DetailOrderProductResponse?>, t: Throwable) {
                callback.detailOrderProductFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<DetailOrderProductResponse?>,
                response: Response<DetailOrderProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailOrderProductSuccess(response.body())
                }else if (response.code() == 500){
                    callback.detailOrderProductFailed("Edit error..")
                }
            }
        })
    }

    fun deleteDetailOrderProduct(id_order: String, id_product: String, callback: DetailOrderProductRepositoryCallback<DetailOrderProductResponse>){
        ApiClient().services.deleteDetailOrderProduct(id_order, id_product).enqueue(object : Callback<DetailOrderProductResponse?>{
            override fun onFailure(call: Call<DetailOrderProductResponse?>, t: Throwable) {
                callback.detailOrderProductFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<DetailOrderProductResponse?>,
                response: Response<DetailOrderProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailOrderProductSuccess(response.body())
                }else if (response.code() == 500){
                    callback.detailOrderProductFailed("Delete error..")
                }
            }
        })
    }

//    CUSTOMER PET
    fun getAllCustomerPet(callback: CustomerPetRepositoryCallback<CustomerPetResponse>){
        ApiClient().services.getAllCustomerPet().enqueue(object : Callback<CustomerPetResponse?> {
            override fun onFailure(call: Call<CustomerPetResponse?>, t: Throwable) {
                callback.customerPetFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<CustomerPetResponse?>,
                response: Response<CustomerPetResponse?>
            ) {
                if (response.isSuccessful){
                    callback.customerPetSuccess(response.body())
                }else if (response.code() == 500){
                    callback.customerPetFailed("Show error..")
                }
            }
        })
    }

    fun getCustomerPetBySearch(query: String, callback: CustomerPetRepositoryCallback<CustomerPetResponse>){
        ApiClient().services.getCustomerPetBySearch(query).enqueue(object : Callback<CustomerPetResponse?>{
            override fun onFailure(call: Call<CustomerPetResponse?>, t: Throwable) {
                callback.customerPetFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<CustomerPetResponse?>,
                response: Response<CustomerPetResponse?>
            ) {
                if (response.isSuccessful){
                    callback.customerPetSuccess(response.body())
                }else if (response.code() == 500){
                    callback.customerPetFailed("Show error..")
                }
            }
        })
    }

    fun addCustomerPet(customerPet: CustomerPet, callback: CustomerPetRepositoryCallback<CustomerPetResponse>){
        ApiClient().services.addCustomerPet(customerPet).enqueue(object : Callback<CustomerPetResponse?>{
            override fun onFailure(call: Call<CustomerPetResponse?>, t: Throwable) {
                callback.customerPetFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<CustomerPetResponse?>,
                response: Response<CustomerPetResponse?>
            ) {
                when {
                    response.isSuccessful -> {
                        callback.customerPetSuccess(response.body())
                    }
                    response.code() == 500 -> {
                        callback.customerPetFailed("Add error..")
                    }
                    response.code() == 404 -> {
                        callback.customerPetFailed("Customer not found..")
                    }
                    else -> callback.customerPetFailed("Else..")
                }
            }
        })
    }

    fun editCustomerPet(id: String, customerPet: CustomerPet, callback: CustomerPetRepositoryCallback<CustomerPetResponse>){
        ApiClient().services.editCustomerPet(id, customerPet).enqueue(object : Callback<CustomerPetResponse?>{
            override fun onFailure(call: Call<CustomerPetResponse?>, t: Throwable) {
                callback.customerPetFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<CustomerPetResponse?>,
                response: Response<CustomerPetResponse?>
            ) {
                when {
                    response.isSuccessful -> {
                        callback.customerPetSuccess(response.body())
                    }
                    response.code() == 500 -> {
                        callback.customerPetFailed("Edit error..")
                    }
                    response.code() == 404 -> {
                        callback.customerPetFailed("Customer not found..")
                    }
                    else -> callback.customerPetFailed("Else..")
                }
            }
        })
    }

    fun deleteCustomerPet(id: String, callback: CustomerPetRepositoryCallback<CustomerPetResponse>){
        ApiClient().services.deleteCustomerPet(id).enqueue(object : Callback<CustomerPetResponse?> {
            override fun onFailure(call: Call<CustomerPetResponse?>, t: Throwable) {
                callback.customerPetFailed(t.message.toString())
            }

            override fun onResponse(
                call: Call<CustomerPetResponse?>,
                response: Response<CustomerPetResponse?>
            ) {
                when {
                    response.isSuccessful -> {
                        callback.customerPetSuccess(response.body())
                    }
                    response.code() == 404 -> {
                        callback.customerPetFailed("Data not found..")
                    }
                    response.code() == 500 -> {
                        callback.customerPetFailed("Delete error..")
                    }
                    response.code() == 406 -> {
                        callback.customerPetFailed("Check constraint..")
                    }
                    else -> callback.customerPetFailed("Else..")
                }
            }
        })
    }

//    TRANSACTION
    fun getAllTransaction(callback: TransactionRepositoryCallback<TransactionResponse>){
        ApiClient().services.getAllTransaction().enqueue(object : Callback<TransactionResponse?>{
            override fun onFailure(call: Call<TransactionResponse?>, t: Throwable) {
                callback.transactionFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<TransactionResponse?>,
                response: Response<TransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.transactionSuccess(response.body())
                }else if (response.code() == 500){
                    callback.transactionFailed("Show error..")
                }
            }
        })
    }

    fun getAllProductTransaction(callback: TransactionRepositoryCallback<TransactionResponse>){
        ApiClient().services.getAllProductTransaction().enqueue(object : Callback<TransactionResponse?>{
            override fun onFailure(call: Call<TransactionResponse?>, t: Throwable) {
                callback.transactionFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<TransactionResponse?>,
                response: Response<TransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.transactionSuccess(response.body())
                }else if (response.code() == 500){
                    callback.transactionFailed("Show error..")
                }
            }
        })
    }

    fun getAllServiceTransaction(callback: TransactionRepositoryCallback<TransactionResponse>){
        ApiClient().services.getAllServiceTransaction().enqueue(object : Callback<TransactionResponse?>{
            override fun onFailure(call: Call<TransactionResponse?>, t: Throwable) {
                callback.transactionFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<TransactionResponse?>,
                response: Response<TransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.transactionSuccess(response.body())
                }else if (response.code() == 500){
                    callback.transactionFailed("Show error..")
                }
            }
        })
    }

    fun addTransaction(type: String, transaction: Transaction, callback: TransactionRepositoryCallback<TransactionResponse>){
        ApiClient().services.addTransaction(type, transaction).enqueue(object : Callback<TransactionResponse?>{
            override fun onFailure(call: Call<TransactionResponse?>, t: Throwable) {
                callback.transactionFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<TransactionResponse?>,
                response: Response<TransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.transactionSuccess(response.body())
                }else if (response.code() == 500){
                    callback.transactionFailed("Show error..")
                }
            }
        })
    }

    fun editTotalTransaction(id: String, callback: TransactionRepositoryCallback<TransactionResponse>){
        ApiClient().services.editTotalTransaction(id).enqueue(object : Callback<TransactionResponse?>{
            override fun onFailure(call: Call<TransactionResponse?>, t: Throwable) {
                callback.transactionFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<TransactionResponse?>,
                response: Response<TransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.transactionSuccess(response.body())
                }else if (response.code() == 500){
                    callback.transactionFailed("Show error..")
                }
            }
        })
    }

    fun editPetTransaction(id: String, transaction: Transaction, callback: TransactionRepositoryCallback<TransactionResponse>){
        ApiClient().services.editPetTransaction(id, transaction).enqueue(object : Callback<TransactionResponse?>{
            override fun onFailure(call: Call<TransactionResponse?>, t: Throwable) {
                callback.transactionFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<TransactionResponse?>,
                response: Response<TransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.transactionSuccess(response.body())
                }else if (response.code() == 500){
                    callback.transactionFailed("Show error..")
                }
            }
        })
    }

    fun editDoneTransaction(id: String, transaction: Transaction, callback: TransactionRepositoryCallback<TransactionResponse>){
        ApiClient().services.editDoneTransaction(id, transaction).enqueue(object : Callback<TransactionResponse?>{
            override fun onFailure(call: Call<TransactionResponse?>, t: Throwable) {
                callback.transactionFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<TransactionResponse?>,
                response: Response<TransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.transactionSuccess(response.body())
                }else if (response.code() == 500){
                    callback.transactionFailed("Show error..")
                }
            }
        })
    }

    fun editStatusTransaction(id: String, transaction: Transaction, callback: TransactionRepositoryCallback<TransactionResponse>){
        ApiClient().services.editStatusTransaction(id, transaction).enqueue(object : Callback<TransactionResponse?>{
            override fun onFailure(call: Call<TransactionResponse?>, t: Throwable) {
                callback.transactionFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<TransactionResponse?>,
                response: Response<TransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.transactionSuccess(response.body())
                }else if (response.code() == 500){
                    callback.transactionFailed("Show error..")
                }
            }
        })
    }

    fun cancelTransaction(id: String, callback: TransactionRepositoryCallback<TransactionResponse>){
        ApiClient().services.cancelTransaction(id).enqueue(object : Callback<TransactionResponse?>{
            override fun onFailure(call: Call<TransactionResponse?>, t: Throwable) {
                callback.transactionFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<TransactionResponse?>,
                response: Response<TransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.transactionSuccess(response.body())
                }else if (response.code() == 500){
                    callback.transactionFailed("Show error..")
                }
            }
        })
    }

//    DETAIL PRODUCT TRANSACTION
    fun getAllDetailProductTransaction(callback: DetailProductTransactionRepositoryCallback<DetailProductTransactionResponse>){
        ApiClient().services.getAllDetailProductTransaction().enqueue(object : Callback<DetailProductTransactionResponse?>{
            override fun onFailure(call: Call<DetailProductTransactionResponse?>, t: Throwable) {
                callback.detailProductTransactionFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<DetailProductTransactionResponse?>,
                response: Response<DetailProductTransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailProductTransactionSuccess(response.body())
                }else if (response.code() == 500){
                    callback.detailProductTransactionFailed("Show error..")
                }
            }
        })
    }

    fun addDetailProductTransaction(detailProductTransaction: DetailProductTransaction, callback: DetailProductTransactionRepositoryCallback<DetailProductTransactionResponse>){
        ApiClient().services.addDetailProductTransaction(detailProductTransaction).enqueue(object : Callback<DetailProductTransactionResponse?>{
            override fun onFailure(call: Call<DetailProductTransactionResponse?>, t: Throwable) {
                callback.detailProductTransactionFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<DetailProductTransactionResponse?>,
                response: Response<DetailProductTransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailProductTransactionSuccess(response.body())
                }else if (response.code() == 500){
                    callback.detailProductTransactionFailed("Show error..")
                }
            }
        })
    }

    fun editDetailProductTransaction(detailProductTransaction: DetailProductTransaction, callback: DetailProductTransactionRepositoryCallback<DetailProductTransactionResponse>){
        ApiClient().services.editDetailProductTransaction(detailProductTransaction).enqueue(object : Callback<DetailProductTransactionResponse?>{
            override fun onFailure(call: Call<DetailProductTransactionResponse?>, t: Throwable) {
                callback.detailProductTransactionFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<DetailProductTransactionResponse?>,
                response: Response<DetailProductTransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailProductTransactionSuccess(response.body())
                }else if (response.code() == 500){
                    callback.detailProductTransactionFailed("Show error..")
                }
            }
        })
    }

    fun deleteDetailProductTransaction(id_transaction: String, id_product: String, callback: DetailProductTransactionRepositoryCallback<DetailProductTransactionResponse>){
        ApiClient().services.deleteDetailProductTransaction(id_transaction, id_product).enqueue(object : Callback<DetailProductTransactionResponse?>{
            override fun onFailure(call: Call<DetailProductTransactionResponse?>, t: Throwable) {
                callback.detailProductTransactionFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<DetailProductTransactionResponse?>,
                response: Response<DetailProductTransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailProductTransactionSuccess(response.body())
                }else if (response.code() == 500){
                    callback.detailProductTransactionFailed("Show error..")
                }
            }
        })
    }

    fun getDetailProductTransactionByIdTransaction(id_transaction: String, callback: DetailProductTransactionRepositoryCallback<DetailProductTransactionResponse>){
        ApiClient().services.getDetailProductTransactionByIdTransaction(id_transaction).enqueue(object : Callback<DetailProductTransactionResponse?>{
            override fun onFailure(call: Call<DetailProductTransactionResponse?>, t: Throwable) {
                callback.detailProductTransactionFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<DetailProductTransactionResponse?>,
                response: Response<DetailProductTransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailProductTransactionSuccess(response.body())
                }else if (response.code() == 500){
                    callback.detailProductTransactionFailed("Show error..")
                }
            }
        })
    }

    fun deleteAllDetailProductTransaction(id_transaction: String, callback: DetailProductTransactionRepositoryCallback<DetailProductTransactionResponse>){
        ApiClient().services.deleteAllDetailProductTransaction(id_transaction).enqueue(object : Callback<DetailProductTransactionResponse?>{
            override fun onFailure(call: Call<DetailProductTransactionResponse?>, t: Throwable) {
                callback.detailProductTransactionFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<DetailProductTransactionResponse?>,
                response: Response<DetailProductTransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailProductTransactionSuccess(response.body())
                }else if (response.code() == 500){
                    callback.detailProductTransactionFailed("Show error..")
                }
            }
        })
    }

//    DETAIL SERVICE TRANSACTION
    fun getAllDetailServiceTransaction(callback: DetailServiceTransactionRepositoryCallback<DetailServiceTransactionResponse>){
        ApiClient().services.getAllDetailServiceTransaction().enqueue(object : Callback<DetailServiceTransactionResponse?>{
            override fun onFailure(call: Call<DetailServiceTransactionResponse?>, t: Throwable) {
                callback.detailServiceTransactionFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<DetailServiceTransactionResponse?>,
                response: Response<DetailServiceTransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailServiceTransactionSuccess(response.body())
                }else if (response.code() == 500){
                    callback.detailServiceTransactionFailed("Show error..")
                }
            }
        })
    }

    fun addDetailServiceTransaction(detailServiceTransaction: DetailServiceTransaction, callback: DetailServiceTransactionRepositoryCallback<DetailServiceTransactionResponse>){
        ApiClient().services.addDetailServiceTransaction(detailServiceTransaction).enqueue(object : Callback<DetailServiceTransactionResponse?>{
            override fun onFailure(call: Call<DetailServiceTransactionResponse?>, t: Throwable) {
                callback.detailServiceTransactionFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<DetailServiceTransactionResponse?>,
                response: Response<DetailServiceTransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailServiceTransactionSuccess(response.body())
                }else if (response.code() == 500){
                    callback.detailServiceTransactionFailed("Show error..")
                }
            }
        })
    }

    fun editDetailServiceTransaction(detailServiceTransaction: DetailServiceTransaction, callback: DetailServiceTransactionRepositoryCallback<DetailServiceTransactionResponse>){
        ApiClient().services.editDetailServiceTransaction(detailServiceTransaction).enqueue(object : Callback<DetailServiceTransactionResponse?>{
            override fun onFailure(call: Call<DetailServiceTransactionResponse?>, t: Throwable) {
                callback.detailServiceTransactionFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<DetailServiceTransactionResponse?>,
                response: Response<DetailServiceTransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailServiceTransactionSuccess(response.body())
                }else if (response.code() == 500){
                    callback.detailServiceTransactionFailed("Show error..")
                }
            }
        })
    }

    fun deleteDetailServiceTransaction(id_transaction: String, id_service: String, callback: DetailServiceTransactionRepositoryCallback<DetailServiceTransactionResponse>){
        ApiClient().services.deleteDetailServiceTransaction(id_transaction, id_service).enqueue(object : Callback<DetailServiceTransactionResponse?>{
            override fun onFailure(call: Call<DetailServiceTransactionResponse?>, t: Throwable) {
                callback.detailServiceTransactionFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<DetailServiceTransactionResponse?>,
                response: Response<DetailServiceTransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailServiceTransactionSuccess(response.body())
                }else if (response.code() == 500){
                    callback.detailServiceTransactionFailed("Show error..")
                }
            }
        })
    }

    fun getDetailServiceTransactionByIdTransaction(id_transaction: String, callback: DetailServiceTransactionRepositoryCallback<DetailServiceTransactionResponse>){
        ApiClient().services.getDetailServiceTransactionByIdTransaction(id_transaction).enqueue(object : Callback<DetailServiceTransactionResponse?>{
            override fun onFailure(call: Call<DetailServiceTransactionResponse?>, t: Throwable) {
                callback.detailServiceTransactionFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<DetailServiceTransactionResponse?>,
                response: Response<DetailServiceTransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailServiceTransactionSuccess(response.body())
                }else if (response.code() == 500){
                    callback.detailServiceTransactionFailed("Show error..")
                }
            }
        })
    }

    fun deleteAllDetailServiceTransaction(id_transaction: String, callback: DetailServiceTransactionRepositoryCallback<DetailServiceTransactionResponse>){
        ApiClient().services.deleteAllDetailServiceTransaction(id_transaction).enqueue(object : Callback<DetailServiceTransactionResponse?>{
            override fun onFailure(call: Call<DetailServiceTransactionResponse?>, t: Throwable) {
                callback.detailServiceTransactionFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<DetailServiceTransactionResponse?>,
                response: Response<DetailServiceTransactionResponse?>
            ) {
                if (response.isSuccessful){
                    callback.detailServiceTransactionSuccess(response.body())
                }else if (response.code() == 500){
                    callback.detailServiceTransactionFailed("Show error..")
                }
            }
        })
    }

//    MIN PRODUCT
    fun getMinProduct(callback: MinProductRepositoryCallback<ProductResponse>){
        ApiClient().services.getMinProduct().enqueue(object : Callback<ProductResponse?>{
            override fun onFailure(call: Call<ProductResponse?>, t: Throwable) {
                callback.minProductFailed(t.message.toString())
            }
            override fun onResponse(
                call: Call<ProductResponse?>,
                response: Response<ProductResponse?>
            ) {
                if (response.isSuccessful){
                    callback.minProductSuccess(response.body())
                }else if (response.code() == 500){
                    callback.minProductFailed("Show error..")
                }
            }
        })
    }

    fun downloadOrderInvoice(id: String, callback: OrderInvoiceRepositoryCallback<ResponseBody>) {
        val baseUrl = "https://gregpetshop.berusahapastibisakok.tech/api/order_invoice/$id"

        ApiClient().services.getInvoiceOrderProduct(id).enqueue(object : Callback<ResponseBody?>{
            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                callback.orderInvoiceFailed(t.message.toString())
            }
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful){
                    callback.orderInvoiceSuccess(response.body())
                }else{
                    callback.orderInvoiceFailed("Download failed..")
                }
            }
        })
    }
}
