package com.example.kouveemanagement.api

import com.example.kouveemanagement.model.*
import okhttp3.MultipartBody
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

    @POST("employee/search")
    fun getEmployeeBySearch(@Field("name")input: String): Call<EmployeeResponse>

    @POST("employee")
    fun addEmployee(@Body employee: Employee): Call<EmployeeResponse>

    @PUT("employee/{id}")
    fun editEmployee(@Path("id")id: String, @Body employee: Employee): Call<EmployeeResponse>

    @DELETE("employee/{id}/{last_emp}")
    fun deleteEmployee(@Path("id")id: String): Call<EmployeeResponse>

//    PRODUCT
    @GET("product")
    fun getAllProduct(): Call<ProductResponse>

    @GET("product/{id}")
    fun getProductById(@Path("id")id: String): Call<ProductResponse>

    @POST("product/search")
    fun getProductBySearch(@Field("name")input: String): Call<ProductResponse>

    @Multipart
    @POST("product/photo/{id}")
    fun uploadPhotoProduct(@Path("id")id: String, @Part photo: MultipartBody.Part): Call<ResponseBody>

    @POST("product")
    fun addProduct(@Body product: Product): Call<ProductResponse>

    @PUT("product/{id}")
    fun editProduct(@Path("id")id: String, @Body product: Product): Call<ProductResponse>

    @DELETE("product/{id}")
    fun deleteProduct(@Path("id")id: String): Call<ProductResponse>

//    CUSTOMER
    @GET("customer")
    fun getAllCustomer(): Call<CustomerResponse>

    @GET("customer/{id}")
    fun getCustomerById(@Path("id")id: String): Call<CustomerResponse>

    @POST("customer/search")
    fun getCustomerBySearch(@Field("name")input: String): Call<CustomerResponse>

    @POST("customer")
    fun addCustomer(@Body customer: Customer): Call<CustomerResponse>

    @PUT("customer/{id}")
    fun editCustomer(@Path("id")id: String, @Body customer: Customer): Call<CustomerResponse>

    @DELETE("customer/{id}/{last_emp}")
    fun deleteCustomer(@Path("id")id: String, @Path("last_emp")last_Emp: String): Call<CustomerResponse>

//    PET SIZE
    @GET("pet_size")
    fun getAllPetSize(): Call<PetSizeResponse>

    @GET("pet_size/{id}")
    fun getPetSizeById(@Path("id")id: String): Call<PetSizeResponse>

    @POST("pet_size/search")
    fun getPetSizeBySearch(@Field("name")input: String): Call<PetSizeResponse>

    @POST("pet_size")
    fun addPetSize(@Body petSize: PetSize): Call<PetSizeResponse>

    @PUT("pet_size/{id}")
    fun editPetSize(@Path("id")id: String, @Body petSize: PetSize): Call<PetSizeResponse>

    @DELETE("pet_size/{id}")
    fun deletePetSize(@Path("id")id: String): Call<PetSizeResponse>

//    PET TYPE
    @GET("pet_type")
    fun getAllPetType(): Call<PetTypeResponse>

    @GET("pet_type/{id}")
    fun getPetTypeById(@Path("id")id: String): Call<PetTypeResponse>

    @POST("pet_type/search")
    fun getPetTypeBySearch(@Field("name")input: String): Call<PetTypeResponse>

    @POST("pet_type")
    fun addPetType(@Body petType: PetType): Call<PetTypeResponse>

    @PUT("pet_type/{id}")
    fun editPetType(@Path("id")id: String, @Body petType: PetType): Call<PetTypeResponse>

    @DELETE("pet_type/{id}")
    fun deletePetType(@Path("id")id: String): Call<PetTypeResponse>

//    SUPPLIER
    @GET("supplier")
    fun getAllSupplier(): Call<SupplierResponse>

    @GET("supplier/{id}")
    fun getSupplierById(@Path("id")id: String): Call<SupplierResponse>

    @POST("supplier/search")
    fun getSupplierBySearch(@Field("name")input: String): Call<SupplierResponse>

    @POST("supplier")
    fun addSupplier(@Body supplier: Supplier): Call<SupplierResponse>

    @PUT("supplier/{id}")
    fun editSupplier(@Path("id")id: String, @Body supplier: Supplier): Call<SupplierResponse>

    @DELETE("supplier/{id}")
    fun deleteSupplier(@Path("id")id: String): Call<SupplierResponse>

//    SERVICE
    @GET("service")
    fun getAllService(): Call<ServiceResponse>

    @GET("service/{id}")
    fun getServiceById(@Path("id")id: String): Call<ServiceResponse>

    @POST("service/search")
    fun getServiceBySearch(@Field("name")input: String): Call<ServiceResponse>

    @POST("service")
    fun addService(@Body service: Service): Call<ServiceResponse>

    @PUT("service/{id}")
    fun editService(@Path("id")id: String, @Body service: Service): Call<ServiceResponse>

    @DELETE("service/{id}")
    fun deleteService(@Path("id")id: String): Call<ServiceResponse>

//    CUSTOMER PET
    @GET("customer_pet")
    fun getAllCustomerPet(): Call<CustomerPetResponse>

    @GET("customer_pet/{id}")
    fun getCustomerPetById(@Path("id")id: String): Call<CustomerPetResponse>

    @POST("customer_pet/search")
    fun getCustomerPetBySearch(@Field("name")input: String): Call<CustomerPetResponse>

    @POST("customer_pet")
    fun addCustomerPet(@Body customerPet: CustomerPet): Call<CustomerPetResponse>

    @PUT("customer_pet/{id}")
    fun editCustomerPet(@Path("id")id: String, @Body customerPet: CustomerPet): Call<CustomerPetResponse>

    @DELETE("customer_pet/{id}")
    fun deleteCustomerPet(@Path("id")id: String): Call<CustomerPetResponse>
}
