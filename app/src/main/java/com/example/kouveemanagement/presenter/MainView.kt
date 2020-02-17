package com.example.kouveemanagement.presenter

import com.example.kouveemanagement.model.EmployeeResponse
import com.example.kouveemanagement.model.LoginResponse
import com.example.kouveemanagement.repository.EmployeeRepositoryCallback
import com.example.kouveemanagement.repository.LoginRepositoryCallback

interface LoginView : LoginRepositoryCallback<LoginResponse> {
    fun showLoading()
    fun hideLoading()
}

interface EmployeeView : EmployeeRepositoryCallback<EmployeeResponse> {
    fun showLoading()
    fun hideLoading()
}