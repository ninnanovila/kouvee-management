package com.example.kouveemanagement.employee

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kouveemanagement.OwnerActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Employee
import com.example.kouveemanagement.model.EmployeeResponse
import com.example.kouveemanagement.presenter.EmployeePresenter
import com.example.kouveemanagement.presenter.EmployeeView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_edit_employee.*
import org.jetbrains.anko.startActivity
import java.util.*

class EditEmployeeActivity : AppCompatActivity(), EmployeeView {

    private lateinit var id: String
    private lateinit var presenter: EmployeePresenter
    private lateinit var employee: Employee

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_employee)
        presenter = EmployeePresenter(this, Repository())
        setData()
        showDatePicker()
        btn_save.setOnClickListener {
            getData()
            presenter.editEmployee(id, employee)
        }
        btn_delete.setOnClickListener {
            presenter.deleteEmployee(id)
        }
        btn_home.setOnClickListener {
            startActivity<OwnerActivity>()
        }
    }

    private fun setData(){
        val employee: Employee? = intent.getParcelableExtra("employee")
        id = employee?.id.toString()
        name.setText(employee?.name)
        address.setText(employee?.address)
        birthdate.setText(employee?.birthdate)
        phone_number.setText(employee?.phone_number)
        role.setText(employee?.role)
        created_at.text = employee?.created_at
        updated_at.text = employee?.updated_at
        deleted_at.text = employee?.deleted_at
    }

    fun getData(){
        val name = name.text.toString()
        val address = address.text.toString()
        val birthdate = birthdate.text.toString()
        val phone_number = phone_number.text.toString()
        val role = role.text.toString()
        employee = Employee(id, name, address, birthdate, phone_number, role, null)
    }

    private fun showDatePicker(){
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        birthdate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                birthdate.setText("$year-$month-$dayOfMonth")
            }, year, month,day)
            datePickerDialog.show()
        }
    }

    override fun showEmployeeLoading() {
        progressbar.visibility = View.VISIBLE
        btn_save.visibility = View.INVISIBLE
        btn_delete.visibility = View.INVISIBLE
    }

    override fun hideEmployeeLoading() {
        progressbar.visibility = View.INVISIBLE
        btn_save.visibility = View.VISIBLE
        btn_delete.visibility = View.VISIBLE
    }

    override fun employeeSuccess(data: EmployeeResponse?) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        startActivity<EmployeeManagementActivity>()
    }

    override fun employeeFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<EmployeeManagementActivity>()
    }


}
