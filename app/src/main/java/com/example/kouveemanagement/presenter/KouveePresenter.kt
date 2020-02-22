package com.example.kouveemanagement.presenter

import com.example.kouveemanagement.model.*
import com.example.kouveemanagement.repository.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody

//LOGIN
class LoginPresenter(private val view: LoginView, private val repository: Repository) {

    fun loginPost(id: String, password: String) {
        view.showLoading()

        repository.loginPost(id, password, object : LoginRepositoryCallback<LoginResponse> {
            override fun loginSuccess(data: LoginResponse?) {
                view.loginSuccess(data)
                view.hideLoading()
            }

            override fun loginFailed() {
                view.loginFailed()
                view.hideLoading()
            }
        })
    }
}

//EMPLOYEE
class EmployeePresenter(private val view: EmployeeView, private val repository: Repository) {

    fun getAllEmployee(){
        view.showLoading()

        repository.getAllEmployee(object : EmployeeRepositoryCallback<EmployeeResponse> {
            override fun employeeSuccess(data: EmployeeResponse?) {
                view.employeeSuccess(data)
                view.hideLoading()
            }

            override fun employeeFailed() {
                view.employeeFailed()
                view.hideLoading()
            }

        })
    }

    fun addEmployee(employee: Employee){
        view.showLoading()

        repository.addEmployee(employee, object : EmployeeRepositoryCallback<EmployeeResponse> {
            override fun employeeSuccess(data: EmployeeResponse?) {
                view.employeeSuccess(data)
                view.hideLoading()
            }

            override fun employeeFailed() {
                view.employeeFailed()
                view.hideLoading()
            }

        })
    }

    fun editEmployee(id: String, employee: Employee){
        view.showLoading()

        repository.editEmployee(id, employee, object : EmployeeRepositoryCallback<EmployeeResponse> {
            override fun employeeSuccess(data: EmployeeResponse?) {
                view.employeeSuccess(data)
                view.hideLoading()
            }

            override fun employeeFailed() {
                view.employeeFailed()
                view.hideLoading()
            }
        })
    }

    fun deleteEmployee(id: String){
        view.showLoading()

        repository.deleteEmployee(id, object : EmployeeRepositoryCallback<EmployeeResponse> {
            override fun employeeSuccess(data: EmployeeResponse?) {
                view.employeeSuccess(data)
                view.hideLoading()
            }

            override fun employeeFailed() {
                view.employeeFailed()
                view.hideLoading()
            }
        })
    }
}

//PRODUCT
class ProductPresenter(private val view : ProductView, private val repository: Repository){

    fun getAllProduct(){
        view.showLoading()

        repository.getAllProduct(object : ProductRepositoryCallback<ProductResponse> {
            override fun productSuccess(data: ProductResponse?) {
                view.productSuccess(data)
                view.hideLoading()
            }

            override fun productFailed() {
                view.productFailed()
                view.hideLoading()
            }
        })
    }

    fun addProduct(product: Product){
        view.showLoading()

        repository.addProduct(product, object : ProductRepositoryCallback<ProductResponse> {
            override fun productSuccess(data: ProductResponse?) {
                view.productSuccess(data)
                view.hideLoading()
            }

            override fun productFailed() {
                view.productFailed()
                view.hideLoading()
            }
        })
    }

    fun editProduct(id: String, product: Product){

        view.showLoading()

        repository.editProduct(id, product, object : ProductRepositoryCallback<ProductResponse> {
            override fun productSuccess(data: ProductResponse?) {
                view.productSuccess(data)
                view.hideLoading()
            }

            override fun productFailed() {
                view.productFailed()
                view.hideLoading()
            }
        })
    }

    fun deleteProduct(id: String){
        view.showLoading()

        repository.deleteProduct(id, object : ProductRepositoryCallback<ProductResponse> {
            override fun productSuccess(data: ProductResponse?) {
                view.productSuccess(data)
                view.hideLoading()
            }

            override fun productFailed() {
                view.productFailed()
                view.hideLoading()
            }
        })
    }


}

//UPLOAD IMAGE
class UploadProductImage(private val view: UploadPhotoProductView, private val repository: Repository){

    fun uploadPhotoProduct(id: String, photo: MultipartBody.Part){
        view.showLoading()

        repository.uploadPhotoProduct(id, photo, object : UploadPhotoProductRepositoryCallback<ResponseBody> {
            override fun uploadProductSuccess(data: ResponseBody?) {
                view.uploadProductSuccess(data)
                view.hideLoading()
            }

            override fun uploadProductFailed() {
                view.uploadProductFailed()
                view.hideLoading()
            }
        })
    }
}

//CUSTOMER
class CustomerPresenter(private val view: CustomerView, private val repository: Repository){

    fun getAllCustomer(){
        view.showLoading()

        repository.getAllCustomer(object : CustomerRepositoryCallback<CustomerResponse> {
            override fun customerSuccess(data: CustomerResponse?) {
                view.customerSuccess(data)
                view.hideLoading()
            }

            override fun customerFailed() {
                view.customerFailed()
                view.hideLoading()
            }
        })
    }

    fun addCustomer(customer: Customer){
        view.showLoading()

        repository.addCustomer(customer,object : CustomerRepositoryCallback<CustomerResponse> {
            override fun customerSuccess(data: CustomerResponse?) {
                view.customerSuccess(data)
                view.hideLoading()
            }

            override fun customerFailed() {
                view.customerFailed()
                view.hideLoading()
            }
        })
    }

    fun editCustomer(id: String, customer: Customer){
        view.showLoading()

        repository.editCustomer(id, customer, object : CustomerRepositoryCallback<CustomerResponse> {
            override fun customerSuccess(data: CustomerResponse?) {
                view.customerSuccess(data)
                view.hideLoading()
            }

            override fun customerFailed() {
                view.customerFailed()
                view.hideLoading()
            }
        })
    }

    fun deleteCustomer(id: String, last_emp: String){
        view.showLoading()

        repository.deleteCustomer(id,last_emp, object : CustomerRepositoryCallback<CustomerResponse> {
            override fun customerSuccess(data: CustomerResponse?) {
                view.customerSuccess(data)
                view.hideLoading()
            }

            override fun customerFailed() {
                view.customerFailed()
                view.hideLoading()
            }
        })
    }
}

//PET SIZE
class PetSizePresenter(private val view: PetSizeView, private val repository: Repository){

    fun getAllPetSize(){
        view.showLoading()

        repository.getAllPetSize(object : PetSizeRepositoryCallback<PetSizeResponse> {
            override fun petSizeSuccess(data: PetSizeResponse?) {
                view.petSizeSuccess(data)
                view.hideLoading()
            }

            override fun petSizeFailed() {
                view.petSizeFailed()
                view.hideLoading()
            }

        })
    }

    fun addPetSize(petSize: PetSize){
        view.showLoading()

        repository.addPetSize(petSize, object : PetSizeRepositoryCallback<PetSizeResponse> {
            override fun petSizeSuccess(data: PetSizeResponse?) {
                view.petSizeSuccess(data)
                view.hideLoading()
            }

            override fun petSizeFailed() {
                view.petSizeFailed()
                view.hideLoading()
            }
        })
    }

    fun editPetSize(id: String, petSize: PetSize){

        view.showLoading()

        repository.editPetSize(id, petSize, object : PetSizeRepositoryCallback<PetSizeResponse> {
            override fun petSizeSuccess(data: PetSizeResponse?) {
                view.petSizeSuccess(data)
                view.hideLoading()
            }

            override fun petSizeFailed() {
                view.petSizeFailed()
                view.hideLoading()
            }
        })
    }

    fun deletePetSize(id: String){

        view.showLoading()

        repository.deletePetSize(id, object : PetSizeRepositoryCallback<PetSizeResponse>{
            override fun petSizeSuccess(data: PetSizeResponse?) {
                view.petSizeSuccess(data)
                view.hideLoading()
            }

            override fun petSizeFailed() {
                view.petSizeFailed()
                view.hideLoading()
            }
        })
    }
}

//PET TYPE
class PetTypePresenter(private val view: PetTypeView, private val repository: Repository){

    fun getAllPetType(){
        view.showLoading()

        repository.getAllPetType(object : PetTypeRepositoryCallback<PetTypeResponse> {
            override fun petTypeSuccess(data: PetTypeResponse?) {
                view.petTypeSuccess(data)
                view.hideLoading()
            }

            override fun petTypeFailed() {
                view.petTypeFailed()
                view.hideLoading()
            }
        })
    }

    fun addPetType(petType: PetType){
        view.showLoading()

        repository.addPetType(petType, object : PetTypeRepositoryCallback<PetTypeResponse> {
            override fun petTypeSuccess(data: PetTypeResponse?) {
                view.petTypeSuccess(data)
                view.hideLoading()
            }

            override fun petTypeFailed() {
                view.petTypeFailed()
                view.hideLoading()
            }
        })
    }

    fun editPetType(id: String, petType: PetType){
        view.showLoading()

        repository.editPetType(id, petType, object : PetTypeRepositoryCallback<PetTypeResponse> {
            override fun petTypeSuccess(data: PetTypeResponse?) {
                view.petTypeSuccess(data)
                view.hideLoading()
            }

            override fun petTypeFailed() {
                view.petTypeFailed()
                view.hideLoading()
            }
        })
    }

    fun deletePetType(id: String){
        view.showLoading()

        repository.deletePetType(id, object : PetTypeRepositoryCallback<PetTypeResponse> {
            override fun petTypeSuccess(data: PetTypeResponse?) {
                view.petTypeSuccess(data)
                view.hideLoading()
            }

            override fun petTypeFailed() {
                view.petTypeFailed()
                view.hideLoading()
            }
        })
    }
}