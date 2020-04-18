package com.example.kouveemanagement.employee

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kouveemanagement.CustomFun
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
    private var roles: Array<String> = arrayOf("Customer Service", "Cashier")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_employee)
        presenter = EmployeePresenter(this, Repository())
        setData()
        showDatePicker()
        btn_save.setOnClickListener {
            if (isValid()){
                getData()
                presenter.editEmployee(id, employee)
            }
        }
        btn_cancel.setOnClickListener {
            presenter.deleteEmployee(id)
        }
        btn_home.setOnClickListener {
            startActivity<OwnerActivity>()
        }
    }

    private fun setDropdown(roleInput: String?){
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)
        role_dropdown.setAdapter(adapter)
        role_dropdown.setText(roleInput, true)
        role_dropdown.setOnItemClickListener { _, _, position, _ ->
            val roleName = roles[position]
            Toast.makeText(this, "Role : $roleName", Toast.LENGTH_LONG).show()
        }
    }

    private fun setData(){
        val employee: Employee? = intent.getParcelableExtra("employee")
        id = employee?.id.toString()
        name.setText(employee?.name)
        address.setText(employee?.address)
        birthdate.setText(employee?.birthdate)
        phone_number.setText(employee?.phone_number)
        setDropdown(employee?.role)
        created_at.setText(employee?.created_at)
        updated_at.setText(employee?.updated_at)
        if(employee?.deleted_at.isNullOrBlank()){
            deleted_at.setText("-")
        }else{
            deleted_at.setText(employee?.deleted_at)
        }
    }

    fun getData(){
        val name = name.text.toString()
        val address = address.text.toString()
        val birthdate = birthdate.text.toString()
        val phoneNumber = phone_number.text.toString()
        val role = role_dropdown.text.toString()
        employee = Employee(id, name, address, birthdate, phoneNumber, role, null)
    }

    private fun isValid(): Boolean {
        if (name.text.isNullOrEmpty()){
            name.error = getString(R.string.error_name)
            return false
        }
        if (address.text.isNullOrEmpty()){
            address.error = getString(R.string.error_address)
            return false
        }
        if (birthdate.text.isNullOrEmpty()){
            birthdate.error = getString(R.string.error_birthdate)
            return false
        }
        if (phone_number.text.isNullOrEmpty()){
            phone_number.error = getString(R.string.error_phone_number)
            return false
        }
        return true
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
        btn_save.startAnimation()
        btn_cancel.visibility = View.INVISIBLE
    }

    override fun hideEmployeeLoading() {
    }

    override fun employeeSuccess(data: EmployeeResponse?) {
        startActivity<EmployeeManagementActivity>()
    }

    override fun employeeFailed(data: String) {
        btn_save.revertAnimation()
        btn_cancel.visibility = View.VISIBLE
        CustomFun.failedSnackBar(container, baseContext, data)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<EmployeeManagementActivity>()
    }


}
