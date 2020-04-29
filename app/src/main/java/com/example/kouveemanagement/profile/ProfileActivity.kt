package com.example.kouveemanagement.profile

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.kouveemanagement.*
import com.example.kouveemanagement.model.Employee
import com.example.kouveemanagement.model.EmployeeResponse
import com.example.kouveemanagement.presenter.EmployeePresenter
import com.example.kouveemanagement.presenter.EmployeeView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_profile.*
import org.jetbrains.anko.startActivity

class ProfileActivity : AppCompatActivity(), EmployeeView {

    private lateinit var idEmployee: String
    private lateinit var employee: Employee
    private var presenter = EmployeePresenter(this, Repository())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        btn_home.setOnClickListener {
            if (employee.role == "Admin"){
                startActivity<OwnerActivity>()
            }else if (employee.role == "Customer Service"){
                startActivity<CustomerServiceActivity>()
            }
        }
        btn_logout.setOnClickListener {
            showLogoutConfirm()
        }
        btn_change.setOnClickListener {
            val fragment: Fragment = ChangePasswordFragment.newInstance()
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment).commit()
        }
        getCurrentUser()
    }

    private fun getCurrentUser(){
        if (OwnerActivity.currentUser?.user_id != null){
            idEmployee = OwnerActivity.currentUser?.user_id.toString()
        }else if (CustomerServiceActivity.currentUser?.user_id != null){
            idEmployee = CustomerServiceActivity.currentUser?.user_id.toString()
        }
        presenter.getEmployeeById(idEmployee)
    }

    private fun setData(input: Employee){
        id.text = input.id.toString()
        name.text = input.name.toString()
        address.text = input.address.toString()
        birthdate.text = input.birthdate.toString()
        phone_number.text = input.phone_number.toString()
        role.text = input.role.toString()
    }

    private fun showLogoutConfirm(){
        val confirm = AlertDialog.Builder(this)
            .setIcon(R.drawable.alert)
            .setTitle("Confirmation")
            .setMessage("Are you sure to log out ?")
            .setCancelable(false)
        confirm.setNegativeButton("NO") { _, _ ->
        }
        confirm.setPositiveButton("YES") { dialog, _ ->
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancelAll()
            val thread = Thread {
                if (employee.role.equals("Admin")){
                    OwnerActivity.currentUser?.let { OwnerActivity.database?.currentUserDao()?.deleteCurrentUser(it) }
                    OwnerActivity.database?.clearAllTables()
                }else if (employee.role.equals("Customer Service")){
                    CustomerServiceActivity.currentUser?.let { CustomerServiceActivity.database?.currentUserDao()?.deleteCurrentUser(it) }
                    CustomerServiceActivity.database?.clearAllTables()
                }
                startActivity<MainActivity>()
            }
            thread.start()
            dialog.dismiss()
        }
        confirm.show()
    }

    override fun showEmployeeLoading() {
        CustomFun.warningSnackBar(container, baseContext, "Load data ...")
    }

    override fun hideEmployeeLoading() {
    }

    override fun employeeSuccess(data: EmployeeResponse?) {
        val temp = data?.employees?.get(0)
        if (temp != null) {
            employee = temp
            setData(temp)
        }
        CustomFun.successSnackBar(container, baseContext, "Done load data ...")
    }

    override fun employeeFailed(data: String) {
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (employee.role == "Admin"){
            startActivity<OwnerActivity>()
        }else if (employee.role == "Customer Service"){
            startActivity<CustomerServiceActivity>()
        }
    }
}
