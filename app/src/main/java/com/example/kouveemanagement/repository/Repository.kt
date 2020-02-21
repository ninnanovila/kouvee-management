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

}