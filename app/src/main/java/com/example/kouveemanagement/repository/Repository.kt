package com.example.kouveemanagement.repository

import com.example.kouveemanagement.api.ApiClient
import com.example.kouveemanagement.model.Employee
import com.example.kouveemanagement.model.EmployeeResponse
import com.example.kouveemanagement.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository {

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

    fun deleteEmployee(id: String, last_emp: String, callback: EmployeeRepositoryCallback<EmployeeResponse>) {

        ApiClient().services.deleteEmployee(id, last_emp).enqueue(object : Callback<EmployeeResponse?> {
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
}