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

//  CHANGE PASS
    @FormUrlEncoded
    @POST("employee/changePass")
    fun changePassword(@Field("id")id: String, @Field("password")password: String, @Field("new_password")newPassword: String): Call<LoginResponse>

//    EMPLOYEE
    @GET("employeeAll")
    fun getAllEmployee(): Call<EmployeeResponse>

    @GET("employee/{id}")
    fun getEmployeeById(@Path("id")id: String): Call<EmployeeResponse>

    @FormUrlEncoded
    @POST("employee/search")
    fun getEmployeeBySearch(@Field("name")input: String): Call<EmployeeResponse>

    @POST("employee")
    fun addEmployee(@Body employee: Employee): Call<EmployeeResponse>

    @PUT("employee/{id}")
    fun editEmployee(@Path("id")id: String, @Body employee: Employee): Call<EmployeeResponse>

    @DELETE("employee/{id}")
    fun deleteEmployee(@Path("id")id: String): Call<EmployeeResponse>

//    PRODUCT
    @GET("productAll")
    fun getAllProduct(): Call<ProductResponse>

    @FormUrlEncoded
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
    @GET("customerAll")
    fun getAllCustomer(): Call<CustomerResponse>

    @FormUrlEncoded
    @POST("customer/search")
    fun getCustomerBySearch(@Field("name")input: String): Call<CustomerResponse>

    @POST("customer")
    fun addCustomer(@Body customer: Customer): Call<CustomerResponse>

    @PUT("customer/{id}")
    fun editCustomer(@Path("id")id: String, @Body customer: Customer): Call<CustomerResponse>

    @DELETE("customer/{id}/{last_emp}")
    fun deleteCustomer(@Path("id")id: String, @Path("last_emp")last_Emp: String): Call<CustomerResponse>

//    PET SIZE
    @GET("pet_sizeAll")
    fun getAllPetSize(): Call<PetSizeResponse>

    @FormUrlEncoded
    @POST("pet_size/search")
    fun getPetSizeBySearch(@Field("name")input: String): Call<PetSizeResponse>

    @POST("pet_size")
    fun addPetSize(@Body petSize: PetSize): Call<PetSizeResponse>

    @PUT("pet_size/{id}")
    fun editPetSize(@Path("id")id: String, @Body petSize: PetSize): Call<PetSizeResponse>

    @DELETE("pet_size/{id}")
    fun deletePetSize(@Path("id")id: String): Call<PetSizeResponse>

//    PET TYPE
    @GET("pet_typeAll")
    fun getAllPetType(): Call<PetTypeResponse>

    @FormUrlEncoded
    @POST("pet_type/search")
    fun getPetTypeBySearch(@Field("name")input: String): Call<PetTypeResponse>

    @POST("pet_type")
    fun addPetType(@Body petType: PetType): Call<PetTypeResponse>

    @PUT("pet_type/{id}")
    fun editPetType(@Path("id")id: String, @Body petType: PetType): Call<PetTypeResponse>

    @DELETE("pet_type/{id}")
    fun deletePetType(@Path("id")id: String): Call<PetTypeResponse>

//    SUPPLIER
    @GET("supplierAll")
    fun getAllSupplier(): Call<SupplierResponse>

    @FormUrlEncoded
    @POST("supplier/search")
    fun getSupplierBySearch(@Field("name")input: String): Call<SupplierResponse>

    @POST("supplier")
    fun addSupplier(@Body supplier: Supplier): Call<SupplierResponse>

    @PUT("supplier/{id}")
    fun editSupplier(@Path("id")id: String, @Body supplier: Supplier): Call<SupplierResponse>

    @DELETE("supplier/{id}")
    fun deleteSupplier(@Path("id")id: String): Call<SupplierResponse>

//    SERVICE
    @GET("serviceAll")
    fun getAllService(): Call<ServiceResponse>

    @FormUrlEncoded
    @POST("service/search")
    fun getServiceBySearch(@Field("name")input: String): Call<ServiceResponse>

    @POST("service")
    fun addService(@Body service: Service): Call<ServiceResponse>

    @PUT("service/{id}")
    fun editService(@Path("id")id: String, @Body service: Service): Call<ServiceResponse>

    @DELETE("service/{id}")
    fun deleteService(@Path("id")id: String): Call<ServiceResponse>

//    CUSTOMER PET
    @GET("customer_petAll")
    fun getAllCustomerPet(): Call<CustomerPetResponse>

    @FormUrlEncoded
    @POST("customer_pet/search")
    fun getCustomerPetBySearch(@Field("name")input: String): Call<CustomerPetResponse>

    @POST("customer_pet")
    fun addCustomerPet(@Body customerPet: CustomerPet): Call<CustomerPetResponse>

    @PUT("customer_pet/{id}")
    fun editCustomerPet(@Path("id")id: String, @Body customerPet: CustomerPet): Call<CustomerPetResponse>

    @DELETE("customer_pet/{id}")
    fun deleteCustomerPet(@Path("id")id: String): Call<CustomerPetResponse>

//  ORDER PRODUCT
    @GET("order_product")
    fun getAllOrderProduct(): Call<OrderProductResponse>

    @POST("order_product")
    fun addOrderProduct(@Body orderProduct: OrderProduct): Call<OrderProductResponse>

    @PUT("order_product/{id}")
    fun editOrderProduct(@Path("id")id: String, @Body orderProduct: OrderProduct): Call<OrderProductResponse>

    @DELETE("order_product/{id}")
    fun deleteOrderProduct(@Path("id")id: String): Call<OrderProductResponse>

    @PUT("order_product/total/{id}")
    fun editTotalOrderProduct(@Path("id")id: String): Call<OrderProductResponse>

    @PUT("order_product/done/{id}")
    fun editDoneOrderProduct(@Path("id")id: String): Call<OrderProductResponse>

    @PUT("order_product/print/{id}")
    fun editPrintOrderProduct(@Path("id")id: String): Call<OrderProductResponse>

    @Streaming
    @GET("order_invoice/{id}")
    fun getInvoiceOrderProduct(@Path("id")id: String): Call<ResponseBody>

//  DETAIL ORDER PRODUCT
    @GET("detail_order_product")
    fun getAllDetailOrderProduct(): Call<DetailOrderProductResponse>

    @GET("detail_order_product/{id_order}")
    fun getDetailOrderProductByOrderId(@Path("id_order")id: String): Call<DetailOrderProductResponse>

    @POST("detail_order_product")
    fun addDetailOrderProduct(@Body detailOrderProduct: DetailOrderProduct): Call<DetailOrderProductResponse>

    @PUT("detail_order_product")
    fun editDetailOrderProduct(@Body detailOrderProduct: DetailOrderProduct): Call<DetailOrderProductResponse>

    @DELETE("detail_order_product/{id_order}/{id_product}")
    fun deleteDetailOrderProduct(@Path("id_order")id_order: String, @Path("id_product")id_product: String): Call<DetailOrderProductResponse>

//  TRANSACTION
    @GET("transaction")
    fun getAllTransaction(): Call<TransactionResponse>

    @GET("transaction/product")
    fun getAllProductTransaction(): Call<TransactionResponse>

    @GET("transaction/service")
    fun getAllServiceTransaction(): Call<TransactionResponse>

    @POST("transaction/{type}")
    fun addTransaction(@Path("type")type: String, @Body transaction: Transaction): Call<TransactionResponse>

    @PUT("transaction/{id}")
    fun editTotalTransaction(@Path("id")id: String): Call<TransactionResponse>

    @PUT("transaction/customer/{id}")
    fun editPetTransaction(@Path("id")id: String, @Body transaction: Transaction): Call<TransactionResponse>

    @PUT("transaction/done/{id}")
    fun editDoneTransaction(@Path("id")id: String, @Body transaction: Transaction): Call<TransactionResponse>

    @PUT("transaction/status/{id}")
    fun editStatusTransaction(@Path("id")id: String, @Body transaction: Transaction): Call<TransactionResponse>

    @DELETE("transaction/{id}")
    fun cancelTransaction(@Path("id")id: String): Call<TransactionResponse>

//  DETAIL PRODUCT TRANSACTION
    @GET("detail_product_transaction")
    fun getAllDetailProductTransaction(): Call<DetailProductTransactionResponse>

    @POST("detail_product_transaction")
    fun addDetailProductTransaction(@Body detailProductTransaction: DetailProductTransaction): Call<DetailProductTransactionResponse>

    @PUT("detail_product_transaction")
    fun editDetailProductTransaction(@Body detailProductTransaction: DetailProductTransaction): Call<DetailProductTransactionResponse>

    @DELETE("detail_product_transaction/{id_transaction}/{id_product}")
    fun deleteDetailProductTransaction(@Path("id_transaction")id_transaction: String, @Path("id_product")id_product: String): Call<DetailProductTransactionResponse>

    @GET("detail_product_transaction/{id_transaction}")
    fun getDetailProductTransactionByIdTransaction(@Path("id_transaction")id_transaction: String): Call<DetailProductTransactionResponse>

    @DELETE("detail_product_transaction/{id_transaction}")
    fun deleteAllDetailProductTransaction(@Path("id_transaction")id_transaction: String): Call<DetailProductTransactionResponse>

//  DETAIL SERVICE TRANSACTION
    @GET("detail_service_transaction")
    fun getAllDetailServiceTransaction(): Call<DetailServiceTransactionResponse>

    @POST("detail_service_transaction")
    fun addDetailServiceTransaction(@Body detailServiceTransaction: DetailServiceTransaction): Call<DetailServiceTransactionResponse>

    @PUT("detail_service_transaction")
    fun editDetailServiceTransaction(@Body detailServiceTransaction: DetailServiceTransaction): Call<DetailServiceTransactionResponse>

    @DELETE("detail_service_transaction/{id_transaction}/{id_service}")
    fun deleteDetailServiceTransaction(@Path("id_transaction")id_transaction: String, @Path("id_service")id_service: String): Call<DetailServiceTransactionResponse>

    @GET("detail_service_transaction/{id_transaction}")
    fun getDetailServiceTransactionByIdTransaction(@Path("id_transaction")id_transaction: String): Call<DetailServiceTransactionResponse>

    @DELETE("detail_service_transaction/{id_transaction}")
    fun deleteAllDetailServiceTransaction(@Path("id_transaction")id_transaction: String): Call<DetailServiceTransactionResponse>

//    NOTIFICATION
    @GET("product/stock")
    fun getMinProduct(): Call<ProductResponse>

}
