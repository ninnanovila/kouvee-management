package com.example.kouveemanagement.employee

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Employee
import com.example.kouveemanagement.model.EmployeeResponse
import com.example.kouveemanagement.presenter.EmployeePresenter
import com.example.kouveemanagement.presenter.EmployeeView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_edit_employee.*
import java.util.*

class EditEmployeeActivity : AppCompatActivity(), EmployeeView {

    private lateinit var id: String
    private lateinit var last_emp: String
    private lateinit var presenter: EmployeePresenter
    private lateinit var employee: Employee

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_employee)

        setData()
        showDatePicker()

        btn_save.setOnClickListener {
            getData()

            presenter = EmployeePresenter(this, Repository())
            presenter.editEmployee(id, employee)
        }

        btn_delete.setOnClickListener {
            presenter = EmployeePresenter(this, Repository())
            presenter.deleteEmployee(id, last_emp)
        }
    }

    private fun setData(){
        val employee: Employee? = intent.getParcelableExtra("employee")
        id = employee?.id.toString()
        last_emp = "0"
        name.setText(employee?.name)
        address.setText(employee?.address)
        birthdate.setText(employee?.birthdate)
        phone_number.setText(employee?.phone_number)
        role.setText(employee?.role)
    }

    fun getData(){
        val name = name.text.toString()
        val address = address.text.toString()
        val birthdate = birthdate.text.toString()
        val phone_number = phone_number.text.toString()
        val role = role.text.toString()
        val last_emp = "0"

        employee = Employee(id, name, address, birthdate, phone_number, role, null, last_emp)
    }

    fun showDatePicker(){

        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        birthdate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                birthdate.setText("$year-$month-$dayOfMonth")
            }, year, month,day)
            datePickerDialog.show()
        }
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun employeeSuccess(data: EmployeeResponse?) {

    }

    override fun employeeFailed() {

    }


}
