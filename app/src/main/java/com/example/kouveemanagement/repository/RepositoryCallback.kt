package com.example.kouveemanagement.repository

interface LoginRepositoryCallback<LoginResponse> {
    fun loginSuccess(data: LoginResponse?)
    fun loginFailed()
}

interface EmployeeRepositoryCallback<EmployeeResponse> {
    fun employeeSuccess(data: EmployeeResponse?)
    fun employeeFailed()
}

