package com.example.kouveemanagement.employee


import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Employee
import com.example.kouveemanagement.model.EmployeeResponse
import com.example.kouveemanagement.presenter.EmployeePresenter
import com.example.kouveemanagement.presenter.EmployeeView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.fragment_add_employee.*
import org.jetbrains.anko.support.v4.startActivity
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class AddEmployeeFragment : Fragment(), EmployeeView {

    private lateinit var employee: Employee
    private lateinit var presenter: EmployeePresenter

    companion object {
        fun newInstance() = AddEmployeeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_employee, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_add.setOnClickListener {
            if (isValid()){
                getData()
                presenter = EmployeePresenter(this, Repository())
                presenter.addEmployee(employee)
            }
        }
        btn_close.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
        showDatePicker()
    }

    fun getData(){
        val name = name.text.toString()
        val address = address.text.toString()
        val birthdate = birthdate.text.toString()
        val phoneNumber = phone_number.text.toString()
        val role = role.text.toString()
        employee = Employee(null, name, address, birthdate, phoneNumber, role, birthdate)
    }

    private fun showDatePicker(){
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        birthdate.setOnClickListener {
            val datePickerDialog =
                context?.let { it1 ->
                    DatePickerDialog(it1, DatePickerDialog.OnDateSetListener {
                            _, year, month, dayOfMonth -> birthdate.setText("$year-$month-$dayOfMonth")
                    }, year, month, day)
                }
            datePickerDialog?.show()
        }
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
        if (role.text.isNullOrEmpty()){
            role.error = getString(R.string.error_phone_number)
            return false
        }
        return true
    }

    override fun showEmployeeLoading() {
        btn_add.startAnimation()
    }

    override fun hideEmployeeLoading() {
        btn_add.revertAnimation()
    }

    override fun employeeSuccess(data: EmployeeResponse?) {
        Toast.makeText(context, "Success Employee", Toast.LENGTH_SHORT).show()
        startActivity<EmployeeManagementActivity>()
    }

    override fun employeeFailed() {
        Toast.makeText(context, "Failed Employee", Toast.LENGTH_SHORT).show()
    }

}
