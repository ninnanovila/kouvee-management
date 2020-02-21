package com.example.kouveemanagement.api

import com.example.kouveemanagement.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

//    LOGIN
    @FormUrlEncoded
    @POST("login")
    fun loginPost(@Field("id")id: String, @Field("password")password: String): Call<LoginResponse>

//    EMPLOYEE
    @GET("employee")
    fun getAllEmployee(): Call<EmployeeResponse>

    @GET("employee/{id}")
    fun getEmployeeById(@Path("id")id: String): Call<EmployeeResponse>

    @POST("employee")
    fun addEmployee(@Body employee: Employee): Call<EmployeeResponse>

    @PUT("employee/{id}")
    fun editEmployee(@Path("id")id: String, @Body employee: Employee): Call<EmployeeResponse>

    @DELETE("employee/{id}/{last_emp}")
    fun deleteEmployee(@Path("id")id: String, @Path("last_emp")last_emp: String): Call<EmployeeResponse>

    //    PRODUCT
    @GET("product")
    fun getAllProduct(): Call<ProductResponse>

    @GET("product/{id}")
    fun getProductById(@Path("id")id: String): Call<ProductResponse>

    @Multipart
    @POST("product/photo/{id}")
    fun uploadPhotoProduct(@Path("id")id: String, @Part photo: MultipartBody.Part): Call<ResponseBody>

    @POST("product")
    fun addProduct(@Body product: Product): Call<ProductResponse>

    @PUT("product/{id}")
    fun editProduct(@Path("id")id: String, @Body product: Product): Call<ProductResponse>

    @DELETE("product/{id}/{last_emp}")
    fun deleteProduct(@Path("id")id: String, @Path("last_emp")last_emp: String): Call<ProductResponse>

    //    CUSTOMER
    @GET("customer")
    fun getAllCustomer(): Call<CustomerResponse>

    @GET("customer/{id}")
    fun getCustomerById(@Path("id")id: String): Call<CustomerResponse>

    @POST("customer")
    fun addCustomer(@Body customer: Customer): Call<CustomerResponse>

    @PUT("customer/{id}")
    fun editCustomer(@Path("id")id: String, @Body customer: Customer): Call<CustomerResponse>

    @DELETE("customer/{id}")
    fun deleteCustomer(@Path("id")id: String): Call<CustomerResponse>


}