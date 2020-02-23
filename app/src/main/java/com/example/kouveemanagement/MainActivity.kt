package com.example.kouveemanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.kouveemanagement.model.LoginResponse
import com.example.kouveemanagement.presenter.LoginPresenter
import com.example.kouveemanagement.presenter.LoginView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity(), LoginView {

    private lateinit var loginPresenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginPresenter = LoginPresenter(this, Repository())

        btn_login.setOnClickListener {
            loginPresenter.loginPost(id_login.text.toString(), password_login.text.toString())
        }
    }

    override fun showLoading() {
        btn_login.visibility = View.INVISIBLE
        progressbar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressbar.visibility = View.INVISIBLE
        btn_login.visibility = View.VISIBLE
    }

    override fun loginSuccess(data: LoginResponse?) {
        //start another activity
        Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
        when(data?.employee?.role){
            "Admin" -> startActivity<OwnerActivity>()
            "Customer Service" -> startActivity<CustomerServiceActivity>()
            "Cashier" -> Toast.makeText(this, "Chasier can not log in", Toast.LENGTH_SHORT).show()
        }
    }

    override fun loginFailed() {
        Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
    }


}
