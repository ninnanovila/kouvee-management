package com.example.kouveemanagement.employee

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.Animation
import com.example.kouveemanagement.OwnerActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.EmployeeRecyclerViewAdapter
import com.example.kouveemanagement.model.Employee
import com.example.kouveemanagement.model.EmployeeResponse
import com.example.kouveemanagement.presenter.EmployeePresenter
import com.example.kouveemanagement.presenter.EmployeeView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_employee_management.*
import org.jetbrains.anko.startActivity

class EmployeeManagementActivity : AppCompatActivity(), EmployeeView {

    private var employees: MutableList<Employee> = mutableListOf()
    private lateinit var presenter: EmployeePresenter

    private lateinit var dialog: View
    private var isRotate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_management)
        presenter = EmployeePresenter(this, Repository())
        presenter.getAllEmployee()
        btn_home.setOnClickListener {
            startActivity<OwnerActivity>()
        }
        fabAnimation()
    }

    override fun showLoading() {
        progressbar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressbar.visibility = View.INVISIBLE
    }

    override fun employeeSuccess(data: EmployeeResponse?) {
        val temp: List<Employee> = data?.employees ?: emptyList()
        if (temp.isEmpty()){
            Toast.makeText(this, "No result", Toast.LENGTH_SHORT).show()
        }else{
            for (i in temp.indices){
                employees.add(i, temp[i])
            }
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = EmployeeRecyclerViewAdapter(employees){
                showDialog(it)
                Toast.makeText(this, it.id, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun employeeFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }

    private fun showDialog(employee: Employee){
        dialog = LayoutInflater.from(this).inflate(R.layout.dialog_detail_employee, null)
        val name = dialog.findViewById<TextView>(R.id.name)
        val address = dialog.findViewById<TextView>(R.id.address)
        val birthdate = dialog.findViewById<TextView>(R.id.birthdate)
        val phone_number = dialog.findViewById<TextView>(R.id.phone_number)
        val role = dialog.findViewById<TextView>(R.id.role)
        val btn_close = dialog.findViewById<ImageButton>(R.id.btn_close)
        val btn_edit = dialog.findViewById<Button>(R.id.btn_edit)

        name.text = employee.name.toString()
        address.text = employee.address.toString()
        birthdate.text = employee.birthdate.toString()
        phone_number.text = employee.phone_number.toString()
        role.text = employee.role.toString()

        val infoDialog= AlertDialog.Builder(this)
            .setView(dialog)
            .setTitle("Employee Info")
            .show()

        btn_edit.setOnClickListener {
            startActivity<EditEmployeeActivity>("employee" to employee)
        }

        btn_close.setOnClickListener {
            infoDialog.dismiss()
        }
    }

    private fun fabAnimation(){

        Animation.init(fab_add)
        Animation.init(fab_search)

        fab_menu.setOnClickListener {
            isRotate = Animation.rotateFab(it, !isRotate)
            if (isRotate){
                Animation.showIn(fab_add)
                Animation.showIn(fab_search)
            }else{
                Animation.showOut(fab_add)
                Animation.showOut(fab_search)
            }
        }

        fab_add.setOnClickListener {
            isRotate = Animation.rotateFab(fab_menu, !isRotate)
            Animation.showOut(fab_add)
            Animation.showOut(fab_search)
            val fragment: Fragment = AddEmployeeFragment.newInstance()
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment).commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<OwnerActivity>()
    }
}
