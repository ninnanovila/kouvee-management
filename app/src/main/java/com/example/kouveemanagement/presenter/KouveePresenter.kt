package com.example.kouveemanagement.presenter

import com.example.kouveemanagement.model.*
import com.example.kouveemanagement.repository.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    fun deleteEmployee(id: String, last_emp: String){
        view.showLoading()

        repository.deleteEmployee(id,last_emp, object : EmployeeRepositoryCallback<EmployeeResponse> {
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