package com.example.kouveemanagement.customer

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.kouveemanagement.CustomerServiceActivity
import com.example.kouveemanagement.MainActivity
import com.example.kouveemanagement.OwnerActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Customer
import com.example.kouveemanagement.model.CustomerResponse
import com.example.kouveemanagement.presenter.CustomerPresenter
import com.example.kouveemanagement.presenter.CustomerView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_edit_customer.*
import org.jetbrains.anko.startActivity
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
        presenter = CustomerPresenter(this, Repository())
        last_emp = MainActivity.currentUser?.user_id.toString()
        btn_save.setOnClickListener {
            getData()
            presenter.editCustomer(id, customer)
        }
        btn_delete.setOnClickListener {
            presenter.deleteCustomer(id, last_emp)
        }
        btn_home.setOnClickListener {
            startActivity<CustomerServiceActivity>()
        }
    }

    private fun setData(){
        val customer: Customer? = intent.getParcelableExtra("customer")
        id = customer?.id.toString()
        name.setText(customer?.name)
        address.setText(customer?.address)
        birthdate.setText(customer?.birthdate)
        phone_number.setText(customer?.phone_number)
        created_at.text = customer?.created_at
        updated_at.text = customer?.updated_at
        deleted_at.text = customer?.deleted_at
        last_emptv.text = customer?.last_emp
    }

    fun getData(){
        val name = name.text.toString()
        val address = address.text.toString()
        val birthdate = birthdate.text.toString()
        val phone_number = phone_number.text.toString()
        customer = Customer(id, name, address, birthdate, phone_number, null, null, null, last_emp)
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

    override fun showLoading() {
        progressbar.visibility = View.VISIBLE
        btn_save.visibility = View.INVISIBLE
        btn_delete.visibility = View.INVISIBLE
    }

    override fun hideLoading() {
        progressbar.visibility = View.INVISIBLE
        btn_save.visibility = View.VISIBLE
        btn_delete.visibility = View.VISIBLE
    }

    override fun customerSuccess(data: CustomerResponse?) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        startActivity<CustomerManagementActivity>()
    }

    override fun customerFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<CustomerManagementActivity>()
    }
}
