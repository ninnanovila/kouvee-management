package com.example.kouveemanagement.presenter

import com.example.kouveemanagement.model.Employee
import com.example.kouveemanagement.model.EmployeeResponse
import com.example.kouveemanagement.model.LoginResponse
import com.example.kouveemanagement.repository.EmployeeRepositoryCallback
import com.example.kouveemanagement.repository.LoginRepositoryCallback
import com.example.kouveemanagement.repository.Repository

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