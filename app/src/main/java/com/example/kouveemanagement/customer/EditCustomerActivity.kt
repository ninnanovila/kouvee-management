package com.example.kouveemanagement.customer

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Customer
import com.example.kouveemanagement.model.CustomerResponse
import com.example.kouveemanagement.presenter.CustomerPresenter
import com.example.kouveemanagement.presenter.CustomerView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_edit_customer.*
import java.util.*

class EditCustomerActivity : AppCompatActivity(), CustomerView {

    private lateinit var id: String
    private lateinit var last_emp: String
    private lateinit var presenter: CustomerPresenter
    private lateinit var customer: Customer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_customer)

        setData()
        showDatePicker()

        btn_save.setOnClickListener {
            getData()

            presenter = CustomerPresenter(this, Repository())
            presenter.editCustomer(id, customer)
        }

        btn_delete.setOnClickListener {
            presenter = CustomerPresenter(this, Repository())
            presenter.deleteCustomer(id, last_emp)
        }
    }

    private fun setData(){
        val customer: Customer? = intent.getParcelableExtra("customer")
        id = customer?.id.toString()
        last_emp = "0"
        name.setText(customer?.name)
        address.setText(customer?.address)
        birthdate.setText(customer?.birthdate)
        phone_number.setText(customer?.phone_number)
    }

    fun getData(){
        val name = name.text.toString()
        val address = address.text.toString()
        val birthdate = birthdate.text.toString()
        val phone_number = phone_number.text.toString()
        val last_emp = "0"

        customer = Customer(id, name, address, birthdate, phone_number, null, null, null, last_emp)
    }

    fun showDatePicker(){

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

    override fun showLoading() {

    }

    override fun hideLoading() {
    }

    override fun customerSuccess(data: CustomerResponse?) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
    }

    override fun customerFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }
}
