package com.example.kouveemanagement

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.kouveemanagement.model.Employee
import com.example.kouveemanagement.model.LoginResponse
import com.example.kouveemanagement.persistent.AppDatabase
import com.example.kouveemanagement.persistent.CurrentUser
import com.example.kouveemanagement.presenter.LoginPresenter
import com.example.kouveemanagement.presenter.LoginView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity(), LoginView {

    private lateinit var loginPresenter: LoginPresenter

    companion object {
        var database: AppDatabase? = null
        var currentUser: CurrentUser? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = Room.databaseBuilder(this, AppDatabase::class.java, "kouvee-db").build()
        checkCurrentUser()

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
        Toast.makeText(this, "Success, welcome!", Toast.LENGTH_SHORT).show()
        data?.employee?.let { insertCurrentUser(it) }
        when(data?.employee?.role){
            "Admin" -> startActivity<OwnerActivity>()
            "Customer Service" -> startActivity<CustomerServiceActivity>()
            "Cashier" -> Toast.makeText(this, "Cashier can not log in", Toast.LENGTH_SHORT).show()
        }

        if(data?.employee?.role.equals("Cashier")){
            deleteCurrentUser()
        }
    }

    override fun loginFailed() {
        Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
    }

    private fun checkCurrentUser(){
        val thread = Thread {
            currentUser = database?.currentUserDao()?.getCurrentuser()

            when(currentUser?.user_role){
                "Admin" -> startActivity<OwnerActivity>()
                "Customer Service" -> startActivity<CustomerServiceActivity>()
            }
        }
        thread.start()
    }

    private fun insertCurrentUser(employee: Employee){
        val thread = Thread {
            currentUser = CurrentUser(employee.id.toString(), employee.name.toString(), employee.role.toString())
            database?.clearAllTables()
            database?.currentUserDao()?.insertCurrentUser(currentUser!!)
        }
        thread.start()
    }

    private fun deleteCurrentUser(){
        val thread = Thread {
            database?.clearAllTables()
            currentUser?.let { database?.currentUserDao()?.deleteCurrentUser(it) }
        }
        thread.start()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (currentUser!=null){
            deleteCurrentUser()
            finishAffinity()
        }else{
            finishAffinity()
        }
    }

}
